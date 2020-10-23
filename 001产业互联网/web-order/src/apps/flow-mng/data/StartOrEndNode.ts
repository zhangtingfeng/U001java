import { Colors, Point, Size, Rect, Graphic, Direction, Utils } from "./Common";
import Node from "./Node";

/**
 * 开始节点
 */
export default abstract class StartNode extends Node {
  constructor(p: Point, size: Size) {
    super("", p, size);
  }

  drawShape(ctx: CanvasRenderingContext2D, active: boolean = false) {
    ctx.strokeStyle = active ? this.colorActive : this.colorNormal;
    ctx.fillStyle = this.fillRect ? ctx.strokeStyle : "transparent";

    // Boundrect
    ctx.beginPath();
    Utils.drawRoundedRect(
      ctx,
      {
        x: this.originPoint.x + this.size.cx * -0.5,
        y: this.originPoint.y + this.size.cy * -0.5,
      },
      this.size,
      10
    );
    // ctx.arc(this.originPoint.x, this.originPoint.y, this.size.cx, 0, 360);
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
