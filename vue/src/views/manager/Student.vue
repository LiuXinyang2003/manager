<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入姓名查询" style="width: 200px" v-model="params.name"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="operation" v-if="user.role !='STUDENT'">
      <el-button type="primary" plain @click="handleAdd" v-if="user.role =='ADMIN'">新增</el-button>
      <el-button type="danger" plain @click="delBatch" v-if="user.role =='ADMIN'">批量删除</el-button>
      <el-button type="success" plain @click="exp()">导出报表</el-button>

      <el-upload
          action="http://localhost:9090/student/upload" style="display: inline-block; margin-left: 10px"
          :show-file-list="false"
          :on-success="successUploadTable">
        <el-button type="primary">导入报表</el-button>
      </el-upload>
    </div>

    <div class="table">
      <el-table :data="tableData" strip @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" v-if="user.role =='ADMIN'"></el-table-column>
        <el-table-column prop="id" label="序号" width="70" align="center" sortable></el-table-column>
        <el-table-column prop="username" label="账号"></el-table-column>
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column label="头像">
          <template v-slot="scope">
            <div style="display: flex; align-items: center">
              <el-image style="width: 40px; height: 40px; border-radius: 50%" v-if="scope.row.avatar"
                        :src="scope.row.avatar" :preview-src-list="[scope.row.avatar]"></el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="gender" label="性别"></el-table-column>
        <el-table-column prop="collegeName" label="学院"></el-table-column>
        <el-table-column prop="specialityName" label="专业"></el-table-column>
        <el-table-column prop="className" label="班级"></el-table-column>
        <el-table-column label="操作" align="center" width="180" v-if="user.role =='ADMIN'">
          <template v-slot="scope" >
            <el-button size="mini" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" plain @click="del(scope.row.id)">删除</el-button>
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

    <el-dialog title="学生" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="form" label-width="100px" style="padding-right: 50px" :rules="rules" ref="formRef">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="姓名"></el-input>
        </el-form-item>
        <el-form-item label="头像">
          <el-upload
              class="avatar-uploader"
              :action="$baseUrl + '/files/upload'"
              :headers="{ token: user.token }"
              list-type="picture"
              :on-success="handleAvatarSuccess"
          >
            <el-button type="primary">上传头像</el-button>
          </el-upload>


        </el-form-item>
        <el-form-item label="性别" prop="name">
          <template>
            <el-radio v-model="form.gender" label="男">男</el-radio>
            <el-radio v-model="form.gender" label="女">女</el-radio>
          </template>
        </el-form-item>
        <el-form-item label="学院" prop="name">
          <el-select v-model="form.collegeId" placeholder="请选择学院" style="width: 100%" @change="loadSpecialtiesAndClasses">
            <el-option v-for="college in collegeData" :key="college.id" :label="college.name" :value="college.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="专业" prop="name">
          <el-select v-model="form.specialityId" placeholder="请选择专业" style="width: 100%">
            <el-option v-for="item in specialityData" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="班级" prop="name">
          <el-select v-model="form.classId" placeholder="请选择班级" style="width: 100%">
            <el-option v-for="item in classData" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
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
import request from "@/utils/request";

export default {
  name: "Student",
  data() {
    return {
      gender:'男',
      tableData: [],  // 所有的数据
      total: 0,
      params:{
        name: '',
        role: '',
        pageNum: 1,   // 当前的页码
        pageSize: 10,  // 每页显示的个数
      },
      username: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
        ],
        name: [
          {required: true, message: '请输入姓名', trigger: 'blur'},
        ]
      },
      ids: [],
      collegeData:[],
      specialityData:[],
      classData:[],
      getNew:[],
      newMap:{}
    }
  },
  created() {
    this.load(1),
    this.loadCollege()

    // this.gds()
    // this.getData()
  },
  watch: {
    'form.collegeId': function(newVal) {
      if (newVal) {
        this.loadSpecialtiesAndClasses();
      }
    },
    'form.specialityId': function(newVal) {
      if (newVal) {
        this.loadClasses();
      }
    }
  },
  methods: {
    successUploadTable(res, file, fileList){
      if (res.code === '200') {
        this.$message.success("批量导入成功")
        this.load(1)
      } else {
        this.$message.error(res.msg)
      }
    },


    loadCollege(){
      this.$request.get('/college/selectAll').then(res =>{
        if(res.code==='200'){
          this.collegeData=res.data
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    loadSpecialtiesAndClasses() {
      if (this.form.collegeId) {
        // 发送请求获取对应学院的专业列表
        this.$request.get(`/college/${this.form.collegeId}/specialties`).then(response => {
          this.specialityData = response.data;
          // 如果只有一个专业，则自动选择该专业并加载班级
          // if (this.specialityData.length === 0) {
          //   this.form.specialityId = null
          // }
          // if (this.specialityData.length === 1) {
          //   this.form.specialityId = this.specialityData[0].id;
          //   this.loadClasses();
          // }
          // // 重置班级选择
          // this.form.classId = null;
          // this.classData = [];
        }).catch(error => {
          console.error('加载专业列表失败:', error);
        });
      }
    },

    loadClasses() {
      if (this.form.specialityId) {
        // 发送请求获取对应专业的班级列表
        this.$request.get(`/speciality/${this.form.specialityId}/classes`).then(response => {
          this.classData = response.data;
          // 如果只有一个班级，则自动选择该班级
          // if (this.classData.length === 1) {
          //   this.form.classId = this.classData[0].id;
          // }
        }).catch(error => {
          console.error('加载班级列表失败:', error);
        });
      }
    },
    // loadSpeciality(){
    //   this.$request.get('/speciality/selectAll').then(res =>{
    //     if(res.code === '200'){
    //       this.specialityData=res.data
    //     }else{
    //       this.$message.error(res.msg)
    //     }
    //   })
    // },
    // loadClasses(){
    //   this.$request.get('/classes/selectAll').then(res =>{
    //     if(res.code === '200'){
    //       this.classData=res.data
    //     }else{
    //       this.$message.error(res.msg)
    //     }
    //   })
    // },
    handleAdd() {   // 新增数据
      this.form = {}  // 新增数据的时候清空数据
      this.fromVisible = true   // 打开弹窗
    },
    handleEdit(row) {   // 编辑数据
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      this.fromVisible = true   // 打开弹窗
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/student/update' : '/student/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {  // 表示成功保存
              this.$message.success('保存成功')
              this.load(1)
              this.fromVisible = false
              this.form = {}
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }
      })
    },
    del(id) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/student/delete/' + id).then(res => {
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
      this.ids = rows.map(v => v.id)
    },
    delBatch() {   // 批量删除
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确定批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/student/delete/batch', {data: this.ids}).then(res => {
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
      this.$request.get('/student/selectPage', {
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
    handleAvatarSuccess(response, file, fileList) {
      // 把头像属性换成上传的图片的链接
      this.form.avatar = response.data
    },
    exp(){
      let user = localStorage.getItem("xm-user")
      let token = JSON.parse(user).token
      console.log("token的值为"+token)
      this.params.role = JSON.parse(localStorage.getItem("xm-user")).role
      this.params.username = JSON.parse(localStorage.getItem("xm-user")).name
      this.params.classId = JSON.parse(localStorage.getItem("xm-user")).classId
      location.href = 'http://localhost:9090/student/export?token=' +token+'&'+"role="+this.params.role+'&'+"classId="+this.params.classId
    }
  }
}
</script>

<style scoped>

</style>