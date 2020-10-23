<template>
  <table1 name="platform/msg" :extend="msgData">
    <div class="row" slot="extend-inner">
      <div class="col-md-12 form-group-title">
        <h4 class="bg-gray">消息发送方式</h4>
      </div>
      <div
        class="row"
        style="margin-top: 20px; margin-bottom: 20px"
        slot="extend-inner"
      >
        <span
          v-for="(item, iDX) in sengWayList"
          class="checkbox col-md-3"
          v-bind:key="item.id"
        >
          <checkbox
            v-model="msgSendWayCheckedList[iDX]"
            name="msgDatass"
            :label="item.text"
          />
        </span>
      </div>
    </div>
    <div slot="extend">
      <div class="row">
        <div class="col-md-6">
          <label class="control-label">消息内容</label>
          <textarea
            class="form-control"
            v-model="sendcontent"
            rows="6"
          ></textarea>
        </div>
      </div>
    </div>
  </table1>
</template>

<script>
import { ajax, msg, progress } from "@f/vendor";
import { Table1, Checkbox, dict, comps } from "@f/framework";
import Vue from "Vue";
import util from "../common/util";

let $thisVue = null;

let msgData = {
  $data: {},

  searchActions: [
    {
      name: "query",
      clz: "primary",
      icon: "search",
    },
    {
      name: "create",
      icon: "plus",
      // clz: 'info'
    },
    {
      name: "del",
      icon: "times",
      // clz: 'danger',
      disabled: true,
    },
  ],
  formSave() {

    this.form.msgSendWay = JSON.stringify($thisVue.msgSendWayCheckedList);
    this.form.sendcontent = $thisVue.sendcontent;
    this._DataForm.formSave.apply(this);
    // alert(this.form.sendcontent);
  },
  async formEnter() {
    // alert(msgData.$data.msgSendWayDic);
    // alert(msgData.$data.msgSendWayDic);
    if (msgData.$data.msgSendWayDic && msgData.$data.msgSendWayDic.length > 0) {
      // this.formEnter2();
    } else {
      progress.show();
      msgData.$data.msgSendWayDic = await ajax.get({
        url: "api/basedata/dict?name=msgSendWay",
      })["msgSendWay"];
      progress.hide();
    }

    this._DataForm.formEnter.apply(this);
    //alert(this.form.msgSendWay)
    $thisVue.msgSendWayCheckedList =JSON.parse(this.form.msgSendWay);
    $thisVue.sendcontent = this.form.sendcontent;

  },

};

export default {
  components: { Table1, Checkbox: comps.Checkbox },
  data() {
    return {
      msgData,
      sendcontent: "",
      sengWayList: [],
      msgSendWayCheckedList: [],
    };
  },
  methods: {},
  async mounted() {
    let msgSendWayDic = await ajax.get({
      url: "api/basedata/dict?name=msgSendWay",
    });
    this.sengWayList = msgSendWayDic.msgSendWay;
    for (let i = 0; i < this.sengWayList.length; i++) {
      this.msgSendWayCheckedList.push(0);
    }
   // alert(this.msgSendWayCheckedList);
    //   = new Array(this.msgSendWayList.length);

    // alert( this.sengWayList);
    $thisVue = this;
  },
};
</script>
<style scoped>
.checkbox + .checkbox {
  margin-top: 10px;
}

.checkbox {
  display: inline-block;
}
.checkbox a{
  color:black;
}
</style>
