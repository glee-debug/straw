/*
显示当前用户的问题
 */
let questionsApp = new Vue({
    el:'#questionsApp',
    data: {
        questions:[],
        pageInfo:{},
    },
    methods: {
        loadQuestions:function (pageNum) {
            if(! pageNum){
                pageNum = 1;
            }
            $.ajax({
                url: '/v1/questions/teacherQuestions',
                method: "GET",
                data:{
                    pageNum:pageNum
                },
                success: function (r) {
                    console.log("成功加载数据");
                    console.log(r);
                    if(r.code === OK){
                        questionsApp.questions = r.data.list;
                        questionsApp.pageInfo = r.data;
                        //为question对象添加持续时间属性
                        questionsApp.updateDuration();
                        questionsApp.updateTagImage();
                    }
                }
            });
        },
        updateTagImage:function(){
            let questions = this.questions;
            for(let i=0; i<questions.length; i++){
               let tags = questions[i].tags;
               if(tags){
                   let tagImage = '/img/tags/'+tags[0].id+'.jpg';
                   console.log(tagImage);
                   questions[i].tagImage = tagImage;
               }
            }
        },
        updateDuration:function () {
            let questions = this.questions;
            for(let i=0; i<questions.length; i++){
                addDuration(questions[i]);
            }
        }
    },
    created:function () {
        console.log("执行了方法");
        this.loadQuestions(1);
    }
});










