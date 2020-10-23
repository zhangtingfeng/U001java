import Repository from "./data/Repository";
import Node from "./data/Node";
import Link from "./data/Link";
import StartNode from "./data/StartNode";
import EndNode from "./data/EndNode";
import ActivityNode from "./data/ActivityNode";
import { Point, Size } from "./data/Common";

export default class FlowData {
  static DefaultSize: Size = { cx: 100, cy: 30 };
  static clear(repo: Repository) {
    for (let g of repo.graphics) repo.remove(g);
  }

  public static loadFlow(repo: Repository, flow: any) {
    // 清除
    FlowData.clear(repo);

    // Node
    let i = 0;
    for (let n in flow.nodes) {
      let nd = flow.nodes[n];
      let node = FlowData.node2Repo(nd, i++);
      node.name = nd.name;
      node.data = nd;
      nd.node = node;
      repo.add(node);
    }

    // Link
    for (let n in flow.nodes) {
      let nd = flow.nodes[n];
      for (let ld of nd.links) {
        let nd2 = flow.nodes[ld.nodeId2];
        let link: Link = new Link(nd.node, nd2.node);
        link.name = ld.name;
        link.data = ld;

        repo.add(link);
      }
    }
  }

  static node2Repo(nodeData, idx): Node {
    let node = FlowData[`node2Repo${nodeData.type}`](nodeData, idx);

    return node;
  }

  static pos(p: Point, idx): Point {
    return {
      x: p.x && p.x > 50 ? p.x : 50,
      y: p.y && p.y > 10 ? p.y : 10,
    };
  }

  static node2Repo0(nodeData, idx): StartNode {
    return new StartNode(FlowData.pos(nodeData, idx), FlowData.DefaultSize);
  }
  static node2Repo1(nodeData, idx): EndNode {
    return new EndNode(FlowData.pos(nodeData, idx), FlowData.DefaultSize);
  }
  static node2Repo2(nodeData, idx): ActivityNode {
    return new ActivityNode(
      nodeData.name,
      FlowData.pos(nodeData, idx),
      FlowData.DefaultSize
    );
  }

  static getFlow(repo: Repository) {
    let flow = { nodes: [], links: [] };
    for (let g of repo.graphics) {
      if (g.type === "node") {
        if (!g.data.id) throw `节点[${g.data.name}]需要设置ID`;
        flow.nodes.push({
          x: (g as Node).originPoint.x,
          y: (g as Node).originPoint.y,
          id: g.data.id,
          name: g.data.name,
          type: g.data.type,
          props: g.data.props,
          links: g.data.links,
        });
      } else if (g.type === "link") {
        flow.links.push({
          name: g.name,
          type: g.data.type,
          condition: g.data.condition,
          nodeId1: (g as Link).node1.data.id,
          nodeId2: (g as Link).node2.data.id,
        });
      }
    }
    return flow;
  }
}
