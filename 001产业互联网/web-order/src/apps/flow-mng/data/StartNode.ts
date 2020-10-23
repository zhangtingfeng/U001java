import { Point, Size, Colors } from "./Common";
import StartOrEndNode from "./StartOrEndNode";

/**
 * 开始节点
 */
export default class StartNode extends StartOrEndNode {
  constructor(p: Point, size: Size) {
    super(p, size);
    this.name = "Start";
    this.canLinkTo = false;
    this.colorNormal = Colors.Start;
    this.colorActive = Colors.NodeActive;
  }
}
