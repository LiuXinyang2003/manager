<template>
  <div>
    <div class="search" v-if="user.role !='STUDENT'">
      <el-input placeholder="请输入请假人" style="width: 200px" v-model="params.name"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="operation">
      <el-button type="primary" plain @click="handleAdd">新增</el-button>
      <el-button type="danger" plain @click="delBatch" v-if="user.role !='STUDENT'">批量删除</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" ref="table" strip @selection-change="handleSelectionChange" :row-key="getRowKeys">
        <el-table-column type="selection" width="55" align="center" v-if="user.role !='STUDENT'" :reserve-selection="true"></el-table-column>
        <el-table-column prop="name" label="请假人" show-overflow-tooltip></el-table-column>
        <el-table-column prop="time" label="请假时间" show-overflow-tooltip></el-table-column>
        <el-table-column prop="reason" label="请假原因" show-overflow-tooltip></el-table-column>
        <el-table-column prop="className" label="所属班级" show-overflow-tooltip></el-table-column>
        <el-table-column prop="state" label="请假状态" show-overflow-tooltip></el-table-column>
        <el-table-column label="审核" show-overflow-tooltip v-if="user.role !='STUDENT'">
        <template v-slot="scope">
          <el-button type="success" @click="changeState(scope.row,'审核通过')" :disabled="scope.row.state!=='待审核'">
            审核通过
          </el-button>
          <el-button type="danger" @click="changeState(scope.row,'审核不通过')"
                     :disabled="scope.row.state!=='待审核'">审核不通过
          </el-button>
        </template>
      </el-table-column>

        <el-table-column label="操作" width="180" align="center" v-if="user.role !='STUDENT'">
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
        <el-form-item prop="reason" label="请假原因">
          <el-input v-model="form.reason" autocomplete="off"></el-input>
        </el-form-item>
        <!--        <el-form-item prop="classId" label="所属班级">-->
        <!--          <el-select v-model="form.classId" placeholder="请选择班级" style="width: 100%">-->
        <!--            <el-option v-for="item in classData" :label="item.name" :value="item.id"></el-option>-->
        <!--          </el-select>-->
        <!--        </el-form-item>-->
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
  name: "Awol",
  data() {
    return {
      tableData: [],  // 所有的数据
      total: 0,
      params:{
        name: '',
        username: '',
        role: "",
        pageNum: 1,   // 当前的页码
        pageSize: 10,  // 每页显示的个数
      },
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        name: [
          {required: true, message: '请输入请假原因', trigger: 'blur'},
        ]
      },
      ids: [],
      classData: []
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
      this.$request.put('/awol/updateClassId').then(res => {
        if (res.code === '200') {  // 表示成功保存
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    lei() {
      this.$request.put("/awol/update", this.form).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.$message.success('保存成功')
          this.load(1)
          this.fromVisible = false
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    changeState(row, state) {
      this.form = JSON.parse(JSON.stringify(row));
      this.form.state = state;
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
            url: this.form.id ? '/awol/update' : '/awol/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {  // 表示成功保存
              this.$message.success('保存成功')
              this.updateClassId()
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
        this.$request.delete('/awol/delete/' + id).then(res => {
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
        this.$request.delete('/awol/delete/batch', {data: this.ids}).then(res => {
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
      this.params.username = JSON.parse(localStorage.getItem("xm-user")).name
      this.params.classId = JSON.parse(localStorage.getItem("xm-user")).classId

      this.$request.get('/awol/selectPage', {
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
      this.load(1)
    },
  }
}
</script>
