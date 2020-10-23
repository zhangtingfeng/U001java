import { ajax } from "@f/vendor"

const FLOWS = {}

const TypeViewer = {
  "fee": () => import("./fee"),
  "ticket": () => import("./ticket"),
}

let flow = {
  async get(flowId) {
    if (!FLOWS[flowId]) {

      let data = await ajax.get(`api/order/flow/def/${flowId}`);
      //debugger;
      FLOWS[flowId] = data.flow
    }

    return FLOWS[flowId]
  },

  async getNode(flowId, nodeId) {
    //debugger;
    let flow = await this.get(flowId);
    //debugger;
    return flow.nodes[nodeId]
  },

  getTypeViewer(type) {
    return TypeViewer[type];
  }
}

export default flow
