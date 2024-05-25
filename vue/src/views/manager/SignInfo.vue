<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入签到主题" style="width: 200px" v-model="params.name"></el-input>
      <el-input placeholder="请输入班级" style="width: 200px" v-model="params.classNameP"></el-input>
      <el-input placeholder="请输入名字" style="width: 200px" v-model="params.name2" v-if="user.role !='STUDENT'"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="operation" v-if="user.role !='STUDENT'">
      <el-button type="primary" plain @click="handleAdd" v-if="user.role =='STUDENT'">新增</el-button>
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
      <el-button type="success" plain @click="exp">导出报表</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" ref="table" strip @selection-change="handleSelectionChange" :row-key="getRowKeys">
        <el-table-column type="selection" width="55" align="center" v-if="user.role !='STUDENT'" :reserve-selection="true"></el-table-column>
        <!--        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>-->
        <el-table-column prop="name" label="签到主题" show-overflow-tooltip></el-table-column>
        <el-table-column prop="studentName" label="学生名" show-overflow-tooltip v-if="user.role !='STUDENT'"></el-table-column>
        <el-table-column prop="start" label="签到时间" show-overflow-tooltip></el-table-column>
        <el-table-column prop="overtime" label="截止时间" show-overflow-tooltip></el-table-column>
        <el-table-column prop="className" label="班级" show-overflow-tooltip></el-table-column>
        <el-table-column prop="state" label="签到状态" show-overflow-tooltip></el-table-column>
        <el-table-column label="签到" width="180" align="center" v-if="user.role =='STUDENT'">
          <template v-slot="scope">
            <el-button plain type="primary" @click="changeState(scope.row,'成功签到')" size="mini"
                       :disabled="scope.row.state!=='待签到'">签到</el-button>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="180" align="center" v-if="user.role !='STUDENT'">
          <template v-slot="scope">
            <el-button plain type="primary" @click="handleEdit(scope.row)" size="mini" v-if="user.role =='STUDENT'">编辑</el-button>
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

  </div>
</template>

<script>
export default {
  name: "SignInfo",
  data() {
    return {
      hasSignIn:0,
      signTotal:0,
      tableData: [],  // 所有的数据
      total: 0,
      params:{
        name: '',
        name2: '',
        username: '',
        studentName: '',
        classNameP: '',
        role: "",
        pageNum: 1,   // 当前的页码
        pageSize: 10,  // 每页显示的个数
      },
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        name: [
          {required: true, message: '请输入学院名称', trigger: 'blur'},
        ]
      },
      ids: [],
      classData: [],
      getNew:[],
    }
  },
  created() {
    this.load(1)
    this.updateClassId()
  },
  methods: {

    getRowKeys(row) {
      return row.id;
    },
    updateClassId(){
      this.$request.put('/signInfo/updateClassId',this.form).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.load(1)
          this.fromVisible = false
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    lei() {
      this.$request.put("/signInfo/update", this.form).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.load(1)
          this.fromVisible = false
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    updateState() {
      this.$request.put("/signInfo/updateState", this.form).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.load(1)
          this.fromVisible = false
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    changeState(row, state) {
      this.form = JSON.parse(JSON.stringify(row));
      const now = new Date();
      const formattedDateTime = now.getFullYear() + '-' +
          ('0' + (now.getMonth() + 1)).slice(-2) + '-' +
          ('0' + now.getDate()).slice(-2) + ' ' +
          ('0' + now.getHours()).slice(-2) + ':' +
          ('0' + now.getMinutes()).slice(-2) + ':' +
          ('0' + now.getSeconds()).slice(-2);
      const dateA = new Date(formattedDateTime);
      const dateB = new Date(this.form.overtime);
      console.log(dateA.getTime())
      console.log(dateB.getTime())
      // 比较两个 Date 对象的毫秒值
      if (dateA.getTime() > dateB.getTime()) {
        if(this.form.state == "待签到")
        this.form.state = "签到超时"
      }else{
        this.form.state = state;
      }
      console.log("状态"+this.form)

      console.log("状态"+this.form)
      this.lei();

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
            url: this.form.id ? '/signInfo/update' : '/signInfo/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {  // 表示成功保存
              this.$message.success('保存成功')
              // this.updateClassId()
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
        this.$request.delete('/signInfo/delete/' + id).then(res => {
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
        this.$request.delete('/signInfo/delete/batch', {data: this.ids}).then(res => {
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
      this.params.username = JSON.parse(localStorage.getItem("xm-user")).username
      this.params.studentName = JSON.parse(localStorage.getItem("xm-user")).name
      this.params.classId = JSON.parse(localStorage.getItem("xm-user")).classId
      this.$request.get('/signInfo/selectPage', {
        params: this.params
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.params.name = null
      this.params.name2 = null
      this.params.classNameP = null
      this.load(1)
    },

    handleCurrentChange(pageNum) {
      this.params.pageNum = pageNum
      this.load(pageNum)
    },
    handleSizeChange(pageSize){
      this.params.pageSize = pageSize
      this.load(1)
    },
    exp(){
      let user = localStorage.getItem("xm-user")
      let token = JSON.parse(user).token
      this.params.role = JSON.parse(localStorage.getItem("xm-user")).role
      this.params.username = JSON.parse(localStorage.getItem("xm-user")).username
      this.params.classId = JSON.parse(localStorage.getItem("xm-user")).classId
      location.href = 'http://localhost:9090/signInfo/export?token=' +token+'&'+"role="+this.params.role+'&'+
          "classId="+this.params.classId+'&'+"name="+this.params.name+'&'+"name2="+this.params.name2+
          '&'+"classNameP="+this.params.classNameP+'&'+"username="+this.params.username
    }
  }
}
</script>

<style scoped>

</style>
