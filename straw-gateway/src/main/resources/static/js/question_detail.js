let detailApp=new Vue({
    el:"#detailApp",
    data:{
        question:{}
    },
    methods:{
        loadQuestion:function(){
            // 获得url路径上?之后的内容
            let qid = location.search;
            console.log(qid);
            // 判断id必须存在
            if (!qid){
                alert("请指定问题的id!");
                return;
            }
            qid=qid.substring(1);
            $.ajax({
                url:"/v1/questions/"+qid,
                type:"get",
                success:function(r){
                    console.log(r);
                    if (r.code==OK){
                        detailApp.question=r.data;
                        //detailApp.loadDuration();
                        addDuration(detailApp.question)
                    }else {
                        alert(r.message);
                    }
                }
            })
        }
    },
    created:function () {
        this.loadQuestion();
    }
})

let postAnswerApp = new Vue({
    el:"#postAnswerApp",
    data:{
        hasError:false,
        message:""
    },
    methods: {
        postAnswer:function(){
            let qid = location.search;
            if (!qid){
                this.hasError = true;
                this.message = "问题id不能为空";
                return;//!!!!!!
            }
            qid=qid.substring(1);
            let content = $("#summernote").val();
            if (!content){
                this.hasError = true;
                this.message = "回答内容不能为空";
                return;//!!!!!!
            }
            $.ajax({
              url:"/v1/answers",
              method:"post",
              data:{
                  questionId : qid,
                  content : content
              },
              success:function (r){
                  console.log(r);
                  if (r.code==CREATED){
                      // r.data是新增的Answer对象的json格式
                      // 将这个新增的answer对象添加到下面回答列表的数组中即可实现新增的同时显示在页面上
                      answersApp.answers.push(r.data);
                      addDuration(r.data);
                      // 清空summernote富文本编辑器的内容
                      $("#summernote").summernote("reset");
                      postAnswerApp.hasError = true;
                      postAnswerApp.message = r.message;
                  }else {
                      postAnswerApp.hasError = true;
                      postAnswerApp.message = r.message;
                  }
              }
            })
        }
    }
})

let answersApp = new Vue({
    el:"#answersApp",
    data:{
        answers:[]
    },
    methods:{
        loadAnswers:function(){
            let qid = location.search;
            if (!qid){
                alert("问题id不能为空");
                return;
            }
            qid = qid.substring(1);

            $.ajax({
                url:"/v1/answers/question/"+qid,
                method:"get",
                success:function (r){
                    console.log(r);
                    if (r.code==OK){
                        answersApp.answers=r.data
                        answersApp.loadDuration();
                    }else {
                        alert(r.message);
                    }
                }

            })
        },
        loadDuration:function (){
            let answers = this.answers;
            for (let i=0;i<answers.length;i++){
                addDuration(answers[i]);
            }
        },
        postComment:function (answerId){
            let textarea=$("#addComment"+answerId+" textarea")
            let content = textarea.val();
            if(!answerId || !content){
                alert("信息不全");
                return;
            }
            let form={
                answerId:answerId,
                content:content
            }
            console.log(form);
            $.ajax({
                url:"/v1/comments",
                method:"post",
                data:form,
                success:function(r){
                    console.log(r);
                    if(r.code == CREATED){
                        // 清空taxtarea内容
                        textarea.val("");
                        let comment = r.data;
                        let answers = answersApp.answers;
                        for (let i=0;i<answers.length;i++){
                            if (answers[i].id==answerId){
                                answers[i].comments.push(comment);
                                $("#addComment"+answerId).collapse("hide");
                                break;
                            }
                        }

                    }else{
                        alert(r.message);
                    }
                }
            })
        },
        updateComment:function(commentId,answer,index){
            let textarea = $("#editComment"+commentId+" textarea");
            let content = textarea.val();
            if (!content || !commentId){
                alert("修改失败"+content+commentId);
                return;
            }
            $.ajax({
                url:"/v1/comments/"+commentId+"/update",// 尽量匹配/v1/comments/{id}/update
                type:"post",
                data:{
                    content:content
                },
                success:function (r){
                    console.log(r);
                    if (r.code==OK){
                        // vue.set(要修改的数组,要修改的元素下标,修改后的数据) 刷新效果
                        Vue.set(answer.comments,index,r.data);
                        $("#editComment"+commentId).collapse("hide");
                    }else {
                        alert(r.message);
                    }
                }
            })
        },
        removeComment:function (commentId,answer,index){
            if (!commentId){
                alert("评论id不存在");
                return;
            }
            $.ajax({
                url:"/v1/comments/"+commentId+"/delete",
                type:"get",
                success:function (r){
                    console.log(r);
                    if(r.code==GONE){
                        // js 提供了一个从数组中删除元素的方法
                        // 数组对象.splice(index,num) 从index位置删除num个元素
                        answer.comments.splice(index,1);
                    }else {
                        alert(r.message);
                    }
                }
            })
        },
        answerSolved:function (answerId){
            $.ajax({
                url:"/v1/answers/"+answerId+"/solved",
                method:"get",
                success:function(r){
                    console.log(r);
                    if (r.code==OK){
                        alert(r.message);
                    }else {
                        alert(r.message);
                    }
                }
            })
        }
    },
    created:function(){
        this.loadAnswers();
    }
})
