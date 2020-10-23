<template>
  <div class="box" v-if="state == 1">
    <template v-for="(d) of lists">
      <div class="box-header with-border bg-gray" :key="`1-${d.type.id}`">
        <h3 class="box-title">{{d.type.text}} - 配置</h3>
      </div>
      <div class="box-body" :key="`2-${d.type.id}`">
        <form action @submit.prevent="submitSettings(d)">
          <div class="form-group">
            <label class="control-label">选择流程</label>
            <select2 :options="d.flows" @input="selectFlow($event, d)" :value="d.type.flow" />
          </div>
          <table class="table table-striped table-hover" :key="`3-${d.type.id}`">
            <thead>
              <tr>
                <th width="100px">流程节点</th>
                <th>处理人员</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(n) of d.nodes" :key="n.id">
                <td>{{n.name}}</td>
                <td>
                  <div class="form-group">
                    <select2 :options="users" :multi="true" v-model="n.users" />
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
          <div class="form-group">
            <button class="btn btn-primary">保存</button>
          </div>
        </form>
      </div>
    </template>
  </div>
</template>

<script>
import { ajax, progress, msg } from "@f/vendor";
import { dict, comps } from "@f/framework";

import FlowMng from "../flow";

export default {
  components: { Select2: comps.Select2 },
  data() {
    return {
      state: 0,
      users: [],
      flows: {},
      lists: [],
    };
  },
  mounted() {
    this.init();
  },
  methods: {
    async init() {
      try {
        await this.loadSettings();
        await this.loadFlowTypes();
        this.state = 1;
      } catch (err) {
        console.error(err);
        this.state = -1;
        msg.info("加载数据出错", "error");
      }
    },
    async loadSettings() {
      let datas = await ajax.get("api/order/flow-mng/settings");
      if (datas.users) {
        this.users = datas.users;
      }
      if (datas.flows) {
        for (let f of datas.flows) {
          this.flows[f.flowType] = {
            type: f.flowType,
            flow: f.flowId,
            nodes: {},
          };
        }
      }

      if (datas.nodes) {
        for (let n of datas.nodes) {
          if (!this.flows[n.type]) continue;

          let nodes = this.flows[n.type].nodes;
          if (!nodes[n.nodeId]) nodes[n.nodeId] = { id: n.nodeId, users: "" };
          nodes[n.nodeId].users += n.uid + ",";
        }
      }
    },
    async loadFlowTypes() {
      let datas = await dict.get("flowType");
      let flowTypes = {};
      for (let t of datas) {
        if ("order" === t.id || "trade" === t.id) continue;
        t.flow = "";
        let ft = { type: t, flows: [], nodes: [] };
        flowTypes[t.id] = ft;
        this.lists.push(ft);
      }
      datas = await ajax.post("api/order/meta/flow/query");
      for (let f of datas.flow) {
        if (!flowTypes[f.type]) continue;
        flowTypes[f.type].flows.push({ id: f.id, text: f.name });
      }
      // this.lists.push(
      //   {
      //     t: { id: "fee", text: "付款流程" },
      //     flows: [],
      //     nodes: [],
      //   },
      //   {
      //     t: { id: "ticket", text: "开票流程" },
      //     flows: [],
      //     nodes: [],
      //   }
      // );
      for (let ft of this.lists) {
        this.resetFlowType(ft);
      }
    },
    async resetFlowType(flowType) {
      if (this.flows[flowType.type.id]) {
        flowType.type.flow = this.flows[flowType.type.id].flow;
        this.loadFlow(flowType.type.flow, flowType);
      }
    },
    selectFlow(id, flowSetting) {
      flowSetting.type.flow = id;
      this.loadFlow(id, flowSetting);
    },

    async loadFlow(id, flowSetting) {
      let nodes = [];
      if (id) {
        let flow = await FlowMng.get(id);
        for (let n in flow.nodes) {
          let node = flow.nodes[n];
          if (node.type != "2") continue;

          let users = "";
          if (this.flows[flowSetting.type.id]) {
            if (this.flows[flowSetting.type.id].nodes[node.id]) {
              users = this.flows[flowSetting.type.id].nodes[node.id].users;
            }
          }
          nodes.push({ id: node.id, name: node.name, users });
        }
      }
      flowSetting.nodes = nodes;
    },

    async submitSettings(flow) {
      if (!flow.type.flow) {
        msg.info(`请先选择处理流程!`, "error");
        return;
      }
      let datas = { type: flow.type.id, flow: flow.type.flow, nodes: [] };

      for (let n of flow.nodes) {
        if (!n.users) {
          msg.info(`请先选择节点[${n.name}]的处理人员!`, "error");
          return;
        }
        datas.nodes.push({ id: n.id, users: n.users });
      }
      try {
        await ajax.post({ url: "api/order/flow-mng/settings", data: [datas] });
        msg.info("保存成功");
      } catch (err) {
        console.error(err);
        msg.info("保存出错", "error");
      }
    },
  },
};
</script>

<style lang="less" scoped>
.table .form-group {
  margin-bottom: 0;
}
</style>