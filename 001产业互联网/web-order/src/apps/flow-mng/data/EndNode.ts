import { Point, Size, Colors } from "./Common";
import StartOrEndNode from "./StartOrEndNode";

/**
 * 开始节点
 */
export default class EndNode extends StartOrEndNode {
  constructor(p: Point, size: Size) {
    super(p, size);
    this.name = "End";
    this.canLinkFrom = false;
    this.colorNormal = Colors.End;
    this.colorActive = Colors.NodeActive;
  }
}
