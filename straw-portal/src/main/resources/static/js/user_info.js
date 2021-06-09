let userApp=new Vue({
    el:"#userApp",
    data:{
        user: {}
    },
    methods:{
        loadUserInfo:function (){
            $.ajax({
                url:"/v1/users/me",
                type:"get",
                success:function (r){
                    console.log(r.message);
                    if (r.code==OK){
                        userApp.user=r.data;
                    }else {
                        alert(r.message);
                    }
                }
            })
        }
    },
    created:function () {
        this.loadUserInfo();
    }
})