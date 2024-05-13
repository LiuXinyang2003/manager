<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入签到主题" style="width: 200px" v-model="params.name"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="operation">
      <el-button type="primary" plain @click="handleAdd">新增</el-button>
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="name" label="签到主题" show-overflow-tooltip></el-table-column>
        <el-table-column prop="start" label="签到时间" show-overflow-tooltip></el-table-column>
        <el-table-column prop="overtime" label="截止时间" show-overflow-tooltip></el-table-column>
        <el-table-column prop="className" label="班级" show-overflow-tooltip></el-table-column>
        <el-table-column prop="total" label="班级人数" show-overflow-tooltip>
        </el-table-column>
        <el-table-column prop="state" label="发起签到状态" show-overflow-tooltip></el-table-column>
        <el-table-column label="发起签到" show-overflow-tooltip v-if="user.role !='STUDENT'">
          <template v-slot="scope">
            <el-button type="success" @click="changeState(scope.row,'成功发起签到')"
                       :disabled="scope.row.state!=='待发起'">成功发起签到
            </el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="primary" @click="handleEdit(scope.row)" size="mini">编辑</el-button>
            <el-button plain type="danger" size="mini" @click=del(scope.row.id)>删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="params.pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="params.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
        </el-pagination>
      </div>
    </div>


    <el-dialog title="信息" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
<!--                  <el-form-item prop="classId" label="所属班级">-->
<!--                    <el-select v-model="form.classId" placeholder="请选择班级" style="width: 100%">-->
<!--                      <el-option v-for="item in classData" :label="item.name" :value="item.id"></el-option>-->
<!--                    </el-select>-->
<!--                  </el-form-item>-->
                <el-form-item prop="name" label="签到主题">
                  <el-input v-model="form.name" autocomplete="off"></el-input>
                </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Check",
  data() {
    return {
      hasSignIn:0,
      signTotal:0,
      tableData: [],  // 所有的数据
      total: 0,
      params:{
        name: '',
        role: '',
        pageNum: 1,   // 当前的页码
        pageSize: 10,  // 每页显示的个数
      },
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        name: [
          {required: true, message: '请输入签到主题', trigger: 'blur'},
        ]
      },
      ids: [],
      classData: [],
      studentData: [],
      thisData: {},
      newRow:[],
      sun: {},
      currentDateTime: '',
      getNew:[],
    }
  },
  created() {
    this.load(1)
    this.loadClass()
    this.loadStudent()
  },
  methods: {
    getCurrentDateTime() {
      // 创建一个Date对象表示当前日期和时间
      const now = new Date();

      // 使用toLocaleString方法格式化日期和时间
      this.currentDateTime = now.toLocaleString();

      // 如果你需要特定的格式，可以使用以下方式
      const formattedDateTime = now.getFullYear() + '-' +
        ('0' + (now.getMonth() + 1)).slice(-2) + '-' +
        ('0' + now.getDate()).slice(-2) + ' ' +
        ('0' + now.getHours()).slice(-2) + ':' +
        ('0' + now.getMinutes()).slice(-2) + ':' +
        ('0' + now.getSeconds()).slice(-2);

      this.currentDateTime = formattedDateTime;
      const a = "2024-04-22 12:20:51";
      const dateA = new Date(formattedDateTime);
      const dateB = new Date(a);
      // 比较两个 Date 对象的毫秒值
      if (dateA.getTime() < dateB.getTime()) {
        return '时间A早于时间B';
      } else if (dateA.getTime() > dateB.getTime()) {
        return '时间A晚于时间B';
      } else {
        return '时间A与时间B相同';
      }
    },
    signIn(list,row){
      this.sun={students:list,check:row};
      this.$request.post('/check/signIn',this.sun).then(res =>{
        if (res.code === '200') {  // 表示成功保存
          this.load(1)
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    signIn2(list,row){
      this.thisData={students:list,check:row};
      let jsonSign = JSON.stringify(this.thisData);
      this.$request.post('/check/signIn2',JSON.stringify(this.thisData)).then(res =>{
        if (res.code === '200') {  // 表示成功保存
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    lei() {
     return this.$request.put("/check/update", this.form).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.load(1)
          this.fromVisible = false

          this.newRow=res.data;
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    changeState(row, state) {
      this.form=JSON.parse(JSON.stringify(row))
      this.form.state=state
      this.lei().then(() => {
        // lei 方法成功更新数据库后，可以在这里执行后续操作
        // 如果需要，也可以在这里再次调用 signIn2 方法
        // 注意：确保 this.studentData 和 this.form 在此时是最新的

        this.signIn2(this.studentData, this.newRow);
        const now = new Date();
        const formattedDateTime = now.getFullYear() + '-' +
            ('0' + (now.getMonth() + 1)).slice(-2) + '-' +
            ('0' + now.getDate()).slice(-2) + ' ' +
            ('0' + now.getHours()).slice(-2) + ':' +
            ('0' + now.getMinutes()).slice(-2) + ':' +
            ('0' + now.getSeconds()).slice(-2);
        const dateA = new Date(formattedDateTime);
        const dateB = new Date(this.newRow.overtime);
        // 比较两个 Date 对象的毫秒值
        if (dateA.getTime() > dateB.getTime()) {
          this.overTime()
        }



      }).catch((error) => {
        // 处理更新失败的情况
        console.error("更新数据库失败:", error);
      });



    },
    overTime(){
      this.$request.put('/signInfo/updateState').then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.load(1)
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    loadClass(){
      this.$request.get('/classes/selectAll').then(res =>{
        if(res.code === '200'){
          this.classData=res.data
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    loadStudent(){
      this.$request.get('/student/selectAll').then(res =>{
        if(res.code === '200'){
          this.studentData=res.data
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    updateClassTotal(){
      this.$request.put('/check/updateClassTotal').then(res => {
      if (res.code === '200') {  // 表示成功保存
        this.load(1)
      } else {
        this.$message.error(res.msg)  // 弹出错误的信息
      }
      })
    },
    updateClassId(){
      this.$request.put('/check/updateClassId').then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.load(1)
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    totalSum(){
      this.$request.get("/student/getTotal1").then(res => {
        this.getNew=res;
      })
    },
    handleAdd() {   // 新增数据
      this.form = {}  // 新增数据的时候清空数据
      this.fromVisible = true   // 打开弹窗
    },
    handleEdit(row) {   // 编辑数据
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      this.fromVisible = true   // 打开弹窗
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      // ... 其余代码
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/check/update' : '/check/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {  // 表示成功保存

              this.$message.success('保存成功')
              this.updateClassId()
              this.updateClassTotal()
              this.load(1)
              this.fromVisible = false
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }
      });

    },
    del(id) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/check/delete/' + id).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    handleSelectionChange(rows) {   // 当前选中的所有的行数据
      this.ids = rows.map(v => v.id)   //  [1,2]
    },
    delBatch() {   // 批量删除
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确定批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/check/delete/batch', {data: this.ids}).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.params.pageNum = pageNum
      this.params.role = JSON.parse(localStorage.getItem("xm-user")).role
      this.params.classId = JSON.parse(localStorage.getItem("xm-user")).classId
      this.$request.get('/check/selectPage', {
        params: this.params
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.params.name = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.params.pageNum = pageNum
      this.load(pageNum)
    },
    handleSizeChange(pageSize){
      this.params.pageSize = pageSize
      console.log(pageSize)
      this.load(1)
    },
  }
}
</script>
