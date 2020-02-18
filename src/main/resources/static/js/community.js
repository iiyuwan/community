function replyUp() {
  var questionId=$('#question_id').val();
  var replyContent=$('#replyContent').val();
  sendToServer(questionId,1,replyContent);
}
function sendToServer(targetId,type,content) {
    if(!content){
        alert("请输入回复内容");
        return;
    }
    $.ajax({
        type:"POST",
        url:"/comment",
        contentType:'application/json',
        data:JSON.stringify({
            "parent_id":targetId,
            "content":content,
            "type":type
        }),
        success:function (resp) {
            console.log(resp);
            if(resp.code==200){
                $('#commentArea').hide();
                window.location.reload();
            }else {
                if(resp.code==407){//让其登录并跳回该页面
                    var isLogin= confirm(resp.message);//是否选择登录
                    if(isLogin){
                        //选择登录
                        window.open("https://github.com/login/oauth/authorize?client_id=a8752f093048b522143f&redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                        window.localStorage.setItem("isClose","true");//设置关闭该页面
                    }
                }else{
                    alert(resp.message);
                }
            }
        } ,
        dataType:"json"
    });
}
/*
* 二级评论回复
* */
function replyComment(e) {
   var commentId=e.getAttribute("data-id");
   var content=$("#input-"+commentId).val();
   console.log(content);
   sendToServer(commentId,2,content);
}
//展开二级评论
function collapseSubComment(e) {

   var commentId=  e.getAttribute("data-id");
   var comments=$("#comment-"+commentId);
   if(!comments.hasClass("in")){
       var container=comments;
       if(container.children().length!=1){
           //已加载了二级评论
           $("#comment-"+commentId).addClass("in");
       }else{
           $.getJSON("/comment/"+commentId,function (resp) {
              // console.log(resp);
             $.each(resp.data.reverse(),function (k,v) {//k是index v是每个二级评论
                   var hr=$("<hr/>",{
                       "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12"
                   });
                   var name=$("<span/>",{
                       "class":"commentName",
                       "html":v.user.name
                   });
                   var content=$("<span/>",{
                       "html":v.content
                   });
                   var time=$("<span/>",{
                       "class":"commentTime",
                       "html":moment(v.gmt_create).format('YYYY/MM/DD')

                   });
                   var mediaBody=$("<div/>",{
                       "class":"media-body mediaBody",
                   });

                   mediaBody.append(name);
                   mediaBody.append($("<br/>",{}));
                   mediaBody.append(content);
                   mediaBody.append(time);
                   mediaBody.append(hr);
                   var mediaLeft=$("<div/>",{
                       "class":"media-left"
                   }).append($("<img/>",{
                       "class":"media-object myAvatar",
                       "src":v.user.avatar_url
                   }));
                   var media=$("<div/>",{
                       "class":"media",
                   });
                   media.append(mediaLeft);
                   media.append(mediaBody);
                   var subCommentElem= $("<div/>",{
                       "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12",
                   });
                   subCommentElem.append(media);
                   container.prepend(subCommentElem);
               });
           });
           $("#comment-"+commentId).addClass("in");
       }


   }else{
       comments.removeClass("in");
   }

}
function selectTag(e) {
    var tag=e.getAttribute("data-tag");
    var defaultTag= $("#tag").val();
    if(defaultTag.indexOf(tag)==-1){//判断是否已经添加
        if(defaultTag){
            $("#tag").val(defaultTag+"-"+tag);
        }else{
            $("#tag").val(tag);
        }
    }
}
function showTag() {
    $("#tagLab"). show();
}
function closeTag() {
    $("#tagLab").hide();

}