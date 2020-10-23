<template>
  <div>
    <div
      :class="[
        curReadOnly
          ? 'box collapsed-box boxbackgroundcolor'
          : 'box boxbackgroundcolor',
      ]"
    >
      <div class="box-header width-border bg-lightgreen">
        <h3 class="box-title">付款清单</h3>
        <div class="box-tools pull-right">
          <button type="button" class="btn btn-box-tool" data-widget="collapse">
            <i :class="[curReadOnly ? 'fa fa-plus' : 'fa fa-minus']"></i>
          </button>
        </div>
      </div>
      <div class="box-body">
        <table
          id="textAlignCenter"
          style="border-color: #F5F5F5;!important; border-width:1px !important; border-style:solid !important;;"
          class="table page-table my-table lr-padding"
        >
          <thead>
            <tr style="background-color: #f5f5f5">
              <th>序号</th>
              <th>付款机构</th>
              <th>付款方式</th>
              <th>付款金额(元)</th>
              <th>付款时间</th>
              <th>交易流水号</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, i) in curgettpayment_requestList">
              <td>{{ i + 1 }}</td>
              <td>{{ item.paymentOrg }}</td>
              <td>{{ getPayASTypeDicList(item.type) }}</td>
              <td>{{ item.amount }}</td>
              <td>{{ item.paymentDt }}</td>
              <td>{{ item.paymentNo }}</td>
            </tr>
          </tbody>
        </table>

        <!--
                <div class="row">
                    <div class="col-md-1">
                        <p style="font-weight: bold">序号</p>
                    </div>
                    <div class="col-md-2">
                        <p style="font-weight: bold">付款机构</p>
                    </div>
                    <div class="col-md-2">
                        <p style="font-weight: bold">付款方式</p>

                    </div>
                    <div class="col-md-2">
                        <p style="font-weight: bold">付款金额(元)</p>

                    </div>

                    <div class="col-md-2">
                        <p style="font-weight: bold">付款时间</p>
                    </div>
                    <div class="col-md-2">
                        <p style="font-weight: bold">交易流水号</p>
                    </div>
                </div>

                <div v-for="(item,i) in curgettpayment_requestList" class="row">
                    <div class="col-md-1"><p>{{i+1}}</p></div>
                    <div class="col-md-2"><p>{{item.paymentOrg}}</p></div>
                    <div class="col-md-2"><p>{{item.type}}</p></div>
                    <div class="col-md-2"><p>{{item.amount}}</p></div>
                    <div class="col-md-2"><p>{{item.paymentDt}}</p></div>
                    <div class="col-md-2"><p>{{item.paymentNo}}</p></div>

                </div>
                -->
      </div>
    </div>
  </div>
</template>

<script>
import { ajax, msg, progress } from "@f/vendor";
import { comps, dict, editors } from "@f/framework";
import pubFuncton from "../../pubcommon";

export default {
  components: {
    date: editors.get("date"),
  },
  data() {
    return {
      total: 0,
      order: this.orderFulldetail.order,
      curgettpayment_requestList: [],
      curReadOnly: this.readonly,
      ASTypeDicList: [],
    };
  },
  props: ["view", "id", "readonly", "args", "base", "orderFulldetail"],
  async beforeMount() {
    await this.getASTypeDicList();
    //this.curReadOnly = !this.curReadOnly;
    // let letCheckGet = false;

    let letTempList = this.orderFulldetail.payment_requestList;
    let letPayed = [];
    for (let i = 0; i < letTempList.length; i++) {
      if (letTempList[i].status == "0") {
        ////状态0才是支付成功
        letTempList[i].paymentOrg = await pubFuncton.getDescFunction(
          letTempList[i].paymentOrg
        );
        letPayed.push(letTempList[i]);
      }
    }
    //debugger;
    this.curgettpayment_requestList = letPayed;
    //debugger;
  },

  methods: {
    async getASTypeDicList() {
      //let processgetASTypeDicList = await ajax.get("api/basedata/dict?name=ASType");
      let letDic = await ajax.get({
        url: "api/basedata/dict?name=paymenttype",
      });
      //  debugger;
      this.ASTypeDicList = letDic.paymenttype;
      //如果没有这句代码，select中初始化会是空白的，默认选中就无法实现
    },
    getPayASTypeDicList(argtype) {
      return pubFuncton.getShowTextaboutDicValue(argtype, this.ASTypeDicList);
    },
  },
};
</script>

<style lang="less" scoped>
@import url("@/common/common.less");

.panel-body {
  .row {
    margin: 0 0 100px 0 !important;
  }

  p {
    font-weight: bold;
  }
}

.CSSBox {
  display: flex;
  flex-direction: row;
  -webkit-justify-content: space-between;
  justify-content: space-between;
}

.ButtonSave {
  align-items: flex-end;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s;
}

.fade-enter,
    .fade-leave-to
        /* .fade-leave-active below version 2.1.8 */ {
  opacity: 0;
}

#textAlignCenter th {
  text-align: center;
}

#textAlignCenter td {
  text-align: center;
}
</style>
