<template>
  <div>
    <el-card style="width: 50%">
      <el-form :model="user" label-width="100px" style="padding-right: 50px">
        <div style="margin: 15px; text-align: center">
          <el-upload
              class="avatar-uploader"
              :action="$baseUrl + '/files/upload'"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
          >
            <img v-if="user.avatar" :src="user.avatar" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </div>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="user.username" placeholder="用户名" ></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="user.name" placeholder="姓名" disabled=""></el-input>
        </el-form-item>
        <el-form-item label="学院" prop="name">
          <el-input v-model="user.collegeName" disabled=""></el-input>
        </el-form-item>
        <el-form-item label="专业" prop="name">
          <el-input v-model="user.specialityName" disabled=""></el-input>
        </el-form-item>
        <el-form-item label="班级" prop="name">
          <el-input v-model="user.className" disabled=""></el-input>
        </el-form-item>
        <div style="text-align: center; margin-bottom: 20px">
          <el-button type="primary" @click="update">保 存</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "StudentPerson",
  data() {
    return {
      tableData: [],
      params:{
        name: '',
        total:0,
        pageNum: 1,   // 当前的页码
        pageSize: 10,  // 每页显示的个数
      },
      user: JSON.parse(localStorage.getItem('xm-user') || '{}')
    }
  },
  created() {
    this.show()
  },
  async mounted() {
    console.log(this.user)
    console.log(JSON.stringify(this.user))
    console.log(JSON.parse(JSON.stringify(this.user)))
    console.log(this.user.token)
    // await this.show()
  },
  methods: {
    load(pageNum) {  // 分页查询
      if (pageNum) this.params.pageNum = pageNum
      this.$request.get('/student/selectPage', {
        params: this.params
      }).then(res => {
        console.log(res.data)
        this.tableData = res.data?.list
        console.log(this.tableData)
        this.total = res.data?.total
      })
    },
    update() {
      // 保存当前的用户信息到数据库
      this.$request.put('/student/update', this.user).then(res => {
        if (res.code === '200') {
          // 成功更新
          this.$message.success('保存成功')

          // 更新浏览器缓存里的用户信息
          localStorage.setItem('xm-user', JSON.stringify(this.user))

          // 触发父级的数据更新
          this.$emit('update:user')
        } else {
          this.$message.error(res.msg)
        }
      })
    },

    show(){
      this.$request.get('/student/selectAdd',{
        params: {name:this.user.name}
      }).then(res =>{
        console.log(JSON.parse(JSON.stringify(this.user)))
        Object.keys(res.data).forEach(key => {
          // 如果user对象中有这个属性，就更新它
          if (this.user.hasOwnProperty(key)) {
            this.user[key] = res.data[key];
          }
        });
        console.log(this.user)
      })
    },
    handleAvatarSuccess(response, file, fileList) {
      // 把user的头像属性换成上传的图片的链接
      this.$set(this.user, 'avatar', response.data)
    },
  }
}
</script>

<style scoped>
/deep/.el-form-item__label {
  font-weight: bold;
}
/deep/.el-upload {
  border-radius: 50%;
}
/deep/.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  border-radius: 50%;
}
/deep/.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
  border-radius: 50%;
}
.avatar {
  width: 120px;
  height: 120px;
  display: block;
  border-radius: 50%;
}
</style>