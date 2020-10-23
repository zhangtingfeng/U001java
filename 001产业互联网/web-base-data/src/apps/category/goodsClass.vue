<template>
  <div>
    <div class="row">
      <div class="box-header with-border bg-gray" style="margin:10px;">
        <h4 class="box-title" style="font-size: 15px;">商品分类</h4>
      </div>
    </div>
    <div class="row">
      <div class="col-md-3" style="border-right:1px solid rgb(192,192,192);height:680px;">
        <div class="row" style="margin:10px;">
          <button class="btn btn-primary" @click.prevent="createNode()">
            新增子分类
          </button>
          <button class="btn btn-primary" @click.prevent="updateNode()">
            修改分类
          </button>
          <button class="btn btn-danger" @click.prevent="deleteNode()">
            删除分类
          </button>
        </div>
        <div class="row" style="margin:10px;margin-top:20px;">
          <div id="tree" ></div>
        </div>
      </div>
      <div class="col-md-9">
        <div style="margin-left:30px;margin-top:10px;">
          <div class="row">
            <div class="box-header with-border bg-blue">
              <h4 class="box-title" style="font-size:15px;">属性信息绑定</h4>
            </div>
          </div>
          <div v-if="isCurrentClassLeaf">
          <div class="row" style="margin-top:20px;" v-if="goodsClassForm.id">
            <div class="col-md-12">
              <ul class="nav nav-tabs">
                <li role="presentation" :class="d.id==selectedGroupId?'active':''" v-for="(d, idx) in propGroup" v-bind:key="idx" @click.prevent="selectGroup(d.id)"><a href="#">{{d.text}}</a></li>
              </ul>
            </div>
          </div>
          <div class="row" style="margin-top:20px;" v-if="goodsClassForm.id">
            <div class="col-md-2">
              <div v-if="selectedGroupId">
                <div class="box-header with-border bg-gray">
                  <h4 class="box-title" style="font-size:15px;">属性名称</h4>
                </div>
                <div style="margin:10px;">
                  <div v-for="(d, idx) in goodsProp" v-bind:key="idx" @click="selectProp(d.id)">
                    <label>
                      <input type="checkbox" :value="d.id" v-model="curClassPropData[d.id].checked">
                    </label>
                    <span>{{d.name}}</span>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-md-5">
              <div v-if="selectedPropId">
                <div class="box-header with-border bg-gray">
                  <h4 class="box-title" style="font-size:15px;">属性配置</h4>
                </div>
                <form style="margin:10px;">
                  <div class="form-group">
                    <label for="">显示方式</label>
                    <hstselect :disabled="!curClassPropData[selectedPropId].checked" class="form-control" name="visibleLevel" v-model="curClassPropData[selectedPropId].visibleLevel" args="visibleLevel">
                    </hstselect>
                  </div>
                </form>
              </div>
            </div>
            <div class="col-md-5">
              <div v-if="goodsPropValue.length>0">
                <div class="box-header with-border bg-gray">
                  <h4 class="box-title" style="font-size:15px;">属性值</h4>
                </div>
                <div class="checkbox" v-for="(d, idx) in goodsPropValue" v-bind:key="idx">
                  <label>
                    <input :disabled="!curClassPropData[selectedPropId].checked" type="checkbox" :value="d" v-model="curClassPropData[selectedPropId].propValues">
                    {{d}}
                  </label>
                </div>
              </div>
            </div>
          </div>
          </div>
          <!-- 
          <div class="row">
            <div>{{curClassPropData}}</div>
          </div>
           -->
          <div class="row" style="margin-top:20px;" v-if="goodsProp.length>0">
            <button type="button" class="btn btn-primary" @click.prevent="save()">保存</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 模态框：分类增删改 -->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h4 class="modal-title" id="myModalLabel">{{opTitle}}</h4>
          </div>
          <div class="modal-body">
            <div class="row">
              <div class="form-group col-md-6" >
                <label class="control-label">商品分类名称</label>
                <input class="form-control" type="text" name="name" v-model="goodsClassForm.name"/>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            <button type="button" class="btn btn-primary" @click.prevent="opNode()">保存</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ajax, msg } from "@f/vendor";
import "jstree";
import "jstree/dist/themes/default/style.css";
import { dict, ajaxAsync, views, editors } from "@f/framework";
import Vue from 'vue'

export default {
  components: {
    hstselect: editors.get("select")
  },
  data() {
    return {
      jstreeRef:null,
      opTitle:"",
      goodsClassForm:{ //当前选中的商品分类
        id:"",
        pid:"",
        name:"",
        op:"" //-1删除，0修改，1新增
      },
      selectedGroupId:"",
      selectedPropId:"",
      isCurrentClassLeaf:false,
      propGroup:[], //当前展示的属性组
      goodsProp:[], //当前展示的属性
      goodsPropValue:[], //当前展示的属性值
      oriClassPropValue:[], //原始数据
      
      //每次操作只针对一个特定分类，
      /*
      {
        prop1:{checked:true,visibleLevel:"2",propValues:[]},
        prop2:{checked:true,visibleLevel:"1",propValues:[v1,v2]},
      }
      */
      curClassPropData:{}, //当前选中的属性数据

    };
  },
  methods:{
    save(){
      ajax.post({
        url: "api/basedata/basedata/saveCategory", 
        data: {
          classId:this.goodsClassForm.id,
          classPropData:this.curClassPropData
        }
      }).then(data => {
        //console.log(data);
        msg.info("保存成功");
      }).catch((e) => {
        console.log(e);
        msg.info(e.msg,"error");
      });
    },
    clearChecked(){
      this.curClassPropData={};
    },
    preparePropData(propId){
      if(!this.curClassPropData[propId]){
        this.curClassPropData[propId]={
          checked:false,
          visibleLevel:"",
          propValues:[]
        }
      }
    },
    convert(){//转成checkbox格式
      console.log(this.oriClassPropValue);
      this.oriClassPropValue.forEach(o => {
        this.preparePropData(o.propId);
        this.curClassPropData[o.propId].checked=true;
        this.curClassPropData[o.propId].visibleLevel=o.visibleLevel;
        this.curClassPropData[o.propId].propValues=[];
        if(o.selectValues){
          try{
            this.curClassPropData[o.propId].propValues=JSON.parse(o.selectValues);
          }catch(e){
            console.log(e);
          }
        }
      });
    },
    selectGroup(gid){
      this.goodsProp=[];
      this.goodsPropValue=[];
      this.selectedGroupId=gid;
      this.selectedPropId="";
      ajax.post({
        url: "api/basedata/basedata/getGoodsProp", 
        data: {
          propGroup:gid
        }
      }).then(data => {
        console.log(data);
        this.goodsProp=data.goodsProp;
        this.goodsProp.forEach(o=>{
          this.preparePropData(o.id);
        });
      }).catch((e) => {
        console.log(e);
        msg.info(e.msg,"error");
      });
    },
    selectProp(pid){
      // this.goodsPropValue=[];
      // this.selectedPropId="";
      // ajax.post({
      //   url: "api/basedata/basedata/getGoodsPropValue", 
      //   data: {
      //     propId:pid
      //   }
      // }).then(data => {
      //   console.log(data);
      //   this.selectedPropId=pid;
      //   this.goodsPropValue=data.goodsPropValue;
      //   if(!this.curClassPropData[pid].checked){
      //     this.curClassPropData[pid].visibleLevel="";
      //     this.curClassPropData[pid].propValues=[];
      //   }
      // }).catch((e) => {
      //   console.log(e);
      //   msg.info(e.msg,"error");
      // });

      this.goodsPropValue=[];
      this.selectedPropId="";
      setTimeout(() => {
        this.selectedPropId=pid;
        this.goodsProp.forEach(o=>{
          if(o.id==pid){
            if(o.editorArgs){
              try{
                this.goodsPropValue=JSON.parse(o.editorArgs);
              }catch(e){
                console.log(e);
              }
            }
          }
        });
        if(!this.curClassPropData[pid].checked){
          this.curClassPropData[pid].visibleLevel="";
          this.curClassPropData[pid].propValues=[];
        }
      }, 10);
    },
    async isLeaf(cid){
      let p = await ajax.post({
        url: "api/basedata/basedata/isGoodsClassLeaf",
        data: {
          classId:cid
        }
      });
      return p;
    },
    loadTreeData(){
      ajax.post({
        url: "api/basedata/basedata/getGoodsClassTreeStyle", 
        data: {
        }
      }).then(data => {
        //console.log(data);
        //[{"id" : "root", "parent" : "#", "text" : "root node"}]
        var ref = $.jstree.reference($('#tree'));
        ref.settings.core.data = data.goodsClassTreeStyle;
        ref.deselect_all();
        ref.refresh();
        this.clearForm();
      }).catch((e) => {
        console.log(e);
        msg.info(e.msg,"error");
      });
    },
    selectTreeNode(event,data){
      //console.log(event);
      //console.log(data);
      //data.node.id;
      //data.node.text;
      //data.selected;
      this.goodsClassForm.pid = data.node.parent;
      this.goodsClassForm.id = data.node.id;
      this.goodsClassForm.name = data.node.text;

      this.clearChecked();
      this.goodsProp=[];
      this.goodsPropValue=[];
      this.selectedGroupId="";
      this.selectedPropId="";

      let p = this.isLeaf(this.goodsClassForm.id);
      p.then((boo)=>{
        console.log("isLeaf:"+boo.isGoodsClassLeaf);
        this.isCurrentClassLeaf=!!boo.isGoodsClassLeaf;
        if(this.isCurrentClassLeaf){
          //加载分类选中值
          ajax.post({
            url: "api/basedata/basedata/getGoodsClassProp", 
            data: {
              classId:this.goodsClassForm.id
            }
          }).then(data => {
            console.log(data);
            this.oriClassPropValue=data.goodsClassProp;
            this.convert();//转化为复选框的值
            this.selectGroup("001");
          }).catch((e) => {
            console.log(e);
            msg.info(e.msg,"error");
          });
        }
      });
    },
    clearForm(){
      this.goodsClassForm.pid = "";
      this.goodsClassForm.id = "";
      this.goodsClassForm.name = "";
      this.goodsClassForm.op = "";
    },
    checkNodeSelected(){
      if(!this.goodsClassForm.id){
        msg.info("请选择一个商品分类","error");
        return false;
      }
      return true;
    },
    checkForm(){
      if(this.goodsClassForm.op=="0"||this.goodsClassForm.op=="-1"){
        if(!this.goodsClassForm.id){
          msg.info("请选择一个商品分类","error");
          return false;
        }
      }
      if(!this.goodsClassForm.name){
        msg.info("商品分类名称必填","error");
        return false;
      }
      if(!this.goodsClassForm.pid){
        msg.info("pid缺失","error");
        return false;
      }
      if(!this.goodsClassForm.op){
        msg.info("op缺失","error");
        return false;
      }
      return true;
    },
    createNode(){
      if(!this.checkNodeSelected()){
        return;
      }
      let pid = this.goodsClassForm.id;
      this.clearForm();
      this.goodsClassForm.pid = pid;
      this.goodsClassForm.op = "1";
      this.opTitle="新建子分类";
      $('#myModal').modal('show');
    },
    updateNode(){
      if(!this.checkNodeSelected()){
        return;
      }
      this.goodsClassForm.op = "0";
      this.opTitle="修改分类";
      $('#myModal').modal('show');
    },
    deleteNode(){
      if(!this.checkNodeSelected()){
        return;
      }
      this.goodsClassForm.op = "-1";
      if(confirm("确定要删除'"+this.goodsClassForm.name+"'吗？")){
        this.opNode();
      }
    },
    opNode(){
      if(!this.checkForm()){
        return;
      }
      ajax.post({
        url: "api/basedata/basedata/opNode", 
        data: this.goodsClassForm
      }).then(data => {
        //console.log(data);
        this.goodsClassForm.op="0";
        this.loadTreeData();
        $('#myModal').modal('hide');
      }).catch((e) => {
        console.log(e);
        msg.info(e.msg,"error");
      });
    }
  },
  mounted(){
    $('#tree').jstree({ 
      'core' : {
        'data' : [],
        'check_callback' : true
      }
    }).on("select_node.jstree", this.selectTreeNode);
    this.loadTreeData();
    //加载属性分组
    dict.get("propGroup").then(data => {
      this.propGroup = data;
    });

  }
};
</script>
