import { Colors, Point, Size, Rect, Graphic, Direction } from "./Common";
import Node from "./Node";

/**
 * 处理节点
 */
export default class AcitivityNode extends Node {
  constructor(public name: string, p: Point, size: Size) {
    super(name, p, size);
  }

  drawShape(ctx: CanvasRenderingContext2D, active: boolean) {
    ctx.strokeStyle = active ? this.colorActive : this.colorNormal;
    ctx.fillStyle = this.fillRect ? ctx.strokeStyle : "transparent";

    // Boundrect
    ctx.beginPath();
    ctx.rect(
      this.originPoint.x + this.size.cx * -0.5,
      this.originPoint.y + this.size.cy * -0.5,
      this.size.cx,
      this.size.cy
    );
    ctx.closePath();
    ctx.fill();
    ctx.stroke();
  }
}
