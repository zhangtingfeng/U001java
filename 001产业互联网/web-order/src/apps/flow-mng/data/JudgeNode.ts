import { Colors, Point, Size, Rect, Graphic, Direction } from "./Common";
import Node from "./Node";

/**
 * 判断节点
 */
export default class JudgeNode extends Node {
  constructor(public name: string, p: Point, size: Size) {
    super(name, p, size);
    this.colorNormal = Colors.NodeNormal2;
  }

  drawShape(ctx: CanvasRenderingContext2D, active: boolean = false) {
    ctx.fillStyle = ctx.strokeStyle = active
      ? this.colorActive
      : this.colorNormal;

    // Boundrect
    ctx.beginPath();
    ctx.moveTo(
      this.originPoint.x + this.size.cx * 0,
      this.originPoint.y + this.size.cy * -0.5
    );
    ctx.lineTo(
      this.originPoint.x + this.size.cx * 0.5,
      this.originPoint.y + this.size.cy * 0
    );
    ctx.lineTo(
      this.originPoint.x + this.size.cx * 0,
      this.originPoint.y + this.size.cy * 0.5
    );
    ctx.lineTo(
      this.originPoint.x + this.size.cx * -0.5,
      this.originPoint.y + this.size.cy * 0
    );
    ctx.closePath();
    ctx.fill();
    ctx.stroke();
  }
  hit(ctx: CanvasRenderingContext2D, p: Point): boolean {
    let rs =
      Math.abs(p.x - this.originPoint.x) <= this.size.cx * 0.5 &&
      Math.abs(p.y - this.originPoint.y) <= this.size.cy * 0.5;
    return rs;
  }
}
