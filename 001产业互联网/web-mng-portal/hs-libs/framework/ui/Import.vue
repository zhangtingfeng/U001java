<template>
  <div class="box-body">
    <div style="margin-bottom:10px;">
      <a href="javascrpit:;" v-on:click.prevent="prevHistory">{{$t('back')}}</a>
    </div>
    <div :class="templateDownBtn?'downLoadFile':'nonedownLoadFile'">
      下载模板 <button data-v-938ec300="" class="btn btn-info" v-on:click="downLoadFileToAjaxFile">{{$t('downLoadFileT')}}</button>
    </div>
    <form id="fileUpload" method="post" enctype="multipart/form-data">
      <div class="form-group col-md-6"><label data-v-938ec300="">{{$t('fileLabel')}}</label> 
        <input type="text" name="uploadfile" id="uploadfile" value="" readonly='readonly'/>
        <input type="file" id="upload" name="upload" :accept="accept"
          onchange="document.getElementById('uploadfile').value=this.value.substring(this.value.lastIndexOf('\\')+1)"/>
        <div class="selectFile" v-on:click="selectFileClick">{{$t('file')}}</div>
      </div>
    </form>
    <div class="form-group col-md-12">
      <button data-v-938ec300="" class="btn btn-info" v-on:click="uploadToAjaxFile">{{$t('upload')}}</button>
      <button data-v-14f9b1b8="" class="btn btn-default" v-on:click.prevent="prevHistory"><i data-v-14f9b1b8="" class="fa fa-cancel"></i>{{$t('cancel')}}</button>
    </div>
    <div class="form-group col-md-6" id="uploadMessage"></div>
  </div>
</template>

<script>
import ajax from "@c/ajax";
import progress from "@c/progress"
import msg from "@c/msg"

export default {
  data() {    
    return {templateDownBtn:this.templateDown?"templateDown":""};
  },
  props:['url',"accept", "info","templateDown"],
  methods: {
    selectFileClick:function(){
      $('#upload').trigger("click");
    },
    uploadToAjaxFile:function(){
      var fileName = $("#uploadfile").val();
      if(!fileName){
        msg.info(this.$t("msg3"), "error");
        return ;
      }

      var $self = this;

      var formData = new FormData(); 
      formData.append('upload', $('#upload')[0].files[0]);
      if(this.info){
         formData.append('info', this.info);
      }
      progress.show();

      /**
       * 客户说提示消息时间太短了，所以提示消息都显示在下方的div中
       * 返回消息规则：
       * msg：1 表示文件类型选择错
       * msg: 2 表示文件大小超过5M
       * msg：3 表示上传成功（成功后，还想提示详细信息，可以再加上{txt: ""}）
       * txt："" 自定义字符串
       * res.error==true 表示后台有未捕获异常
       * 之后如果还有特别提示，可以自己定义{msg: 4,5,6...}，已经有过的就别重复定义
       */
      $.ajax({ 
        url: this.url, 
        type:"post", 
        data:formData, 
        processData:false, 
        contentType:false, 
        dataType:"json",
        success:function(res){
          $("#uploadMessage").html("");
          var htmls = $("#uploadMessage").html() + "<p>";
          if(res.error && typeof(res.error) == "object"){
            htmls += $self.$t("servererror");
          }  else if(res.msg == 3){
            //拼接数据
            htmls += fileName + " 上传成功 ";
          } else if(res.msg == 2) {
            htmls += $self.$t("msg2");
          } else if(res.msg == 1) {
            htmls += $self.$t("msg1");
          }

          if(res.txt) {
            htmls += res.txt;
          }
          $("#uploadMessage").html(htmls+ "</p>");
          progress.hide();
          // 上传文件清空
          var obj = document.getElementById('upload') ; 
          obj.outerHTML=obj.outerHTML;
          $("#uploadfile").val("");
        }, 
        error:function(err){
          msg.info("servererror", "error");
          progress.hide();
          // 上传文件清空
          var obj = document.getElementById('upload') ; 
          obj.outerHTML=obj.outerHTML;
          $("#uploadfile").val("");
        }
      });
    },
    downLoadFileToAjaxFile:function(){
      window.open(this.templateDown );
    },
    prevHistory:function(pars){
      this.$emit('prevList', 1);//返回列表页
    }
  }
}
</script>

<style scoped>
.box {
  border-top-width: 1px;
  user-select: text;
}
.box-body{
  min-height: 300px;
}
.selectFile{
		width:100px;
		background-color: #3292E2;
		text-align: center;
		color: #fff;
		height: 30px;
		line-height: 30px;
		position: absolute;
		top: 28px;
		left: 400px;
		cursor: pointer;
	}
#upload{
  display:none;
}
#uploadfile{
  width: 500px;
  height: 38px;
  border: 1px solid #c6c6c6;
}
#uploadfile:hover{
  border-color: #3c8dbc;
  cursor:pointer;
}
.form-group{
  margin-top: 20px;
}
.field-width{
  width: 200px
}
.downLoadFile{
  display: block;
}
.nonedownLoadFile{
  display: none;
}
</style>
<i18n-yaml>
en:
  upload: "Upload"
  fileLabel: "File"
  back: "Back"
  file: "Select File"
  download: "Download Model"
  msg1: "File Type Should Be Excel"
  msg2: "File Size Can not  greater than 5M"
  msg3: "Please Select File"
  downLoadFileT: "DownLoad File"
cn:
  upload: "上传"
  fileLabel: "文件"
  back: "返回"
  file: "选择文件上传"
  msg1: "请选择文件上传，只支持xls、xlsx格式的文件"
  msg2: "上传文件过大，只支持上传5M文件"
  msg3: "请选择上传的文件"
  downLoadFileT: "下载模板"
</i18n-yaml>
