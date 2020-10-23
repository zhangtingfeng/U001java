<template>
  <table1 name="basedata/base-goods-prop" :extend="goodsPropData" >
    <div slot="extend">
      <div class="row">
        <div class="col-md-6">
          <label class="control-label">控件参数</label>
          <textarea class="form-control" v-model="editorArgs" rows="6"></textarea>
        </div>
      </div>
    </div>
  </table1>
</template>

<script>
import { ajax, msg } from "@f/vendor";
import { Table1 } from "@f/framework";

let $thisVue=null;

let goodsPropData = {
  searchActions: [
    {
      name: "query",
      clz: "primary",
      icon: "search"
    },
    {
      name: "create",
      icon: "plus"
      // clz: 'info'
    },
    {
      name: "del",
      icon: "times",
      // clz: 'danger',
      disabled: true
    }
  ],
  formSave(){
    if(this.form.editor=="select"){//下拉列表
      if($thisVue.editorArgs){
        let ary=$thisVue.editorArgs.split(/\s+/);
        this.form.editorArgs=JSON.stringify(ary);
      }
    }else{
      this.form.editorArgs=$thisVue.editorArgs;
    }
    this._DataForm.formSave.apply(this);
  },
  formEnter(){
    this._DataForm.formEnter.apply(this);
    if(this.form.editor=="select"){//下拉列表
      if(this.form.editorArgs){
        $thisVue.editorArgs="";
        let ary = JSON.parse(this.form.editorArgs);
        ary.forEach(s => {
          $thisVue.editorArgs+=s+"\n";
        });
      }
    }else{
      $thisVue.editorArgs=this.form.editorArgs;
    }
  }
};

export default {
  components: { Table1 },
  data() {
    return {
      goodsPropData,
      editorArgs:""
    };
  },
  methods:{
    
  },
  mounted(){
    $thisVue=this;
  }
};
</script>
