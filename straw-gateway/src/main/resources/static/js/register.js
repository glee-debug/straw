let app = new Vue({
    el:'#app',
    data:{
        inviteCode:'',
        phone:'',
        nickname:'',
        password:'',
        confirm:'',
        message: '',
        hasError: false
    },
    methods:{
        register:function () {
            console.log('Submit');
            let data = {
                inviteCode: this.inviteCode,
                phone: this.phone,
                nickname: this.nickname,
                password: this.password,
                confirm: this.confirm
            }
            console.log(data);
            if(data.password !== data.confirm){
                this.message = "确认密码不一致！";
                this.hasError = true;
                return;
            }
            let _this = this;
            $.ajax({
                url:"/sys/v1/users/register",//要访问sys模块的方法 需要在路径上加路由
                method: "POST",
                data: data,
                success: function (r) {
                    console.log(r);
                    if(r.code == OK){
                        console.log("注册成功");
                        console.log(r.message);
                        _this.hasError = false;
                        location.href = '/login.html?register';
                    }else{
                        console.log(r.message);
                        _this.hasError = true;
                        _this.message = r.message;
                    }
                }
            });
        }
    }
});