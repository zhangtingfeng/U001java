<template>
  <div class="flow-viewer">
    <canvas />
  </div>
</template>

<script>
import Layout from "./data/Layout";
import Controll from "./data/Controll";
import { Colors, Point, Size } from "./data/Common";
import Node from "./data/Node";
import Link from "./data/Link";
import ActivityNode from "./data/ActivityNode";
import StartNode from "./data/StartNode";
import EndNode from "./data/EndNode";
import Repository from "./data/Repository";

import FlowData from "./FlowData";
import FlowMng from "../flow";
import { dict } from "@f/framework";

function nodeDrawText(ctx, active) {
  // Text
  ctx.fillStyle = this.colorText;
  ctx.font = Colors.Font;
  // let textWidth = ctx.measureText(this.name).width;
  ctx.fillText(
    this.name,
    this.originPoint.x + this.size.cx * -0.5 + 10,
    this.originPoint.y + this.size.cy * -0.5 + 14
  );

  ctx.beginPath();
  ctx.strokeStyle = this.fillRect ? this.colorText : this.colorNormal;
  ctx.moveTo(
    this.originPoint.x + this.size.cx * -0.5,
    this.originPoint.y + this.size.cy * -0.5 + 22
  );
  ctx.lineTo(
    this.originPoint.x + this.size.cx * 0.5,
    this.originPoint.y + this.size.cy * -0.5 + 22
  );
  ctx.stroke();
  ctx.closePath();

  if (this.descriptions) {
    ctx.fillStyle = this.colorText;
    for (let i in this.descriptions) {
      if (this.descriptions[i]) {
        let txt = this.descriptions[i];
        ctx.fillText(
          txt,
          this.originPoint.x + this.size.cx * -0.5 + 1,
          this.originPoint.y + this.size.cy * -0.5 + 40 + 20 * i
        );
      }
    }
  }
}

export default {
  props: ["flowId", "hists"],
  data() {
    return { flow: null };
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      this.initFlow();
      this.showFlow();
    },

    initFlow() {
      if (!this.flow) {
        let ctx = this.$el.querySelector("canvas").getContext("2d");
        let repo = new Repository();
        let layout = new Layout(ctx, repo);
        let ctl = new Controll(ctx, repo, false);
        this.flow = { repo, layout, ctl };
      }
    },

    async showFlow() {
      let hists = {};
      for (let h of this.hists) {
        hists[h.nodeId] = {
          uid: h.nodeUserid,
          remark: h.remark,
          dt: h.nodeDt,
          nextNode: h.nextNode,
        };
      }

      let flowCfg = await FlowMng.get(this.flowId);
      let users = await dict.getObjs("flow-users");

      let maxHeight = 0;

      FlowData.loadFlow(this.flow.repo, flowCfg);
      for (let g of this.flow.repo.graphics) {
        if (g.type === "node") {
          if (maxHeight < g.originPoint.y) maxHeight = g.originPoint.y;

          g.size.cx = 130;
          g.size.cy = 90;
          g.colorNormal = g.colorActive = Colors.NodeNormal;
          g.fillRect = !!hists[g.data.id];
          g.drawText = nodeDrawText;
          if (hists[g.data.id]) {
            let u = users[hists[g.data.id].uid];
            g.descriptions = [
              (u && u.text) || hists[g.data.id].uid,
              // hists[g.data.id].uid,
              hists[g.data.id].dt,
              hists[g.data.id].remark,
            ];
          } else {
            g.colorText = "#333";
          }
        } else if (g.type === "link") {
          let hist = hists[g.data.nodeId1];
          g.colorNormal = g.colorActive =
            hist && hist.nextNode === g.data.nodeId2 ? "green" : "#ccc";
        }
      }

      let $canvas = this.$el.querySelector("canvas");
      $canvas.width = $canvas.parentElement.clientWidth;
      $canvas.height = maxHeight + 60;

      this.flow.layout.drawAll();
    },
  },
};
</script>
