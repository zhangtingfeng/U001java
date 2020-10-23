<template>
  <div class="box">
    <template v-if="process && process.process">
      <component :is="viewer" :process="process.process" />
    </template>
    <template v-if="btns && btns.length">
      <div class="box-header box-solid bg-gray">
        <h3 class="box-title">流程流转</h3>
      </div>
      <div class="box-body">
        <form action @submit.prevent>
          <div class="form-group">
            <input type="text" class="form-control" placeholder="意见" v-model="remark" />
          </div>
        </form>
      </div>
    </template>
    <div class="box-footer flow-links">
      <a
        class="btn btn-primary"
        href="#"
        v-for="btn of btns"
        :key="btn.id"
        @click.prevent="flowSubmit(btn.id)"
      >{{btn.name || btn.id}}</a>
      <a class="btn btn-default" href="#" @click.prevent="back">返回</a>
    </div>
    <template v-if="process && process.process">
      <div class="box-header box-solid bg-gray">
        <h3 class="box-title">执行流程</h3>
      </div>
      <div class="box-body flow-hists">
        <flow-viewer :flowId="process.process.flowId" :hists="process.hists" />
      </div>
    </template>
  </div>
</template>

<script>
import { ajax, progress, msg } from "@f/vendor";
import FlowMng from "../flow";

import FlowViewer from "./histsViewer";

export default {
  components: { FlowViewer },
  props: ["id"],
  data() {
    return {
      viewer: null,
      process: null,
      flow: null,
      btns: [],
      remark: "",
    };
  },
  mounted() {
    this.init();
  },
  methods: {
    back() {
      history.back();
    },
    async init() {
      try {
        progress.show();
        let process = await ajax.get(`api/order/flow/${this.id}`);
        if (!process.process) throw "流程数据加载出错";

        let flow = await FlowMng.get(process.process.flowId);
        this.viewer = FlowMng.getTypeViewer(flow.type);

        // 如果不控制用户，或者用户在本节点处理用户清单中，显示流转的按钮
        if (
          !process.process.userControl ||
          (process.flow.userNodes &&
            process.flow.userNodes.indexOf(process.process.nodeId) > -1)
        ) {
          let node = await FlowMng.getNode(
            process.process.flowId,
            process.process.nodeId
          );

          if (!node) throw "加载流程信息出错";
          let btns = [];
          for (let l of node.links) {
            if (process.links[l.nodeId2])
              btns.push({ id: l.nodeId2, name: l.name });
          }
          this.btns = btns;
        }
        this.process = process;
      } catch (err) {
        console.error(err);
        msg.info(
          typeof err === "string" ? err : err.msg || "系统错误",
          "error"
        );
      } finally {
        progress.hide();
      }
    },
    async flowSubmit(nodeId) {
      try {
        progress.show();
        let rs = await ajax.post({
          url: `api/order/flow/${this.id}`,
          data: { next: nodeId, remark: this.remark },
        });
        if (rs.rs != "0") throw "流程提交出错";

        this.init();
      } catch (err) {
        console.error(err);
        msg.info(
          typeof err === "string" ? err : err.msg || "系统错误",
          "error"
        );
      } finally {
        progress.hide();
      }
    },
  },
};
</script>

<style lang="less" scoped>
.flow-links {
  a {
    padding-left: 30px;
    padding-right: 30px;
    margin-right: 30px;
  }
}
.form-group {
  margin-bottom: 0;
}
</style>