import { Colors, Point, Size, Rect, Graphic, Direction, Prop } from "./Common";

/**
 * 节点基类
 */
export default abstract class Node extends Graphic {
  /**
   * 节点中心位置
   */
  public originPoint: Point;
  /**
   * 节点大小
   */
  public size: Size;

  /**
   * 可以引出连线
   */
  public canLinkFrom: boolean = true;

  /**
   * 可以引入连线
   */
  public canLinkTo: boolean = true;

  public colorNormal = Colors.NodeNormal;
  public colorActive = Colors.NodeActive;
  public colorText = Colors.Text;
  public font = Colors.Font;
  public fillRect = true;

  constructor(public name: string, p: Point, size: Size) {
    super();
    this.type = "node";
    this.originPoint = p;
    this.size = Object.assign({}, size);
  }
  public toString = (): string => {
    return `Node(${this.name}:${this.originPoint.x},${this.originPoint.y})`;
  };

  /**
   * 获取连接点坐标
   * @param dir 连接方向
   */
  getLinkPoint(dir: Direction, target: boolean = true): Point {
    let cx = 0,
      cy = 0;
    if (target) {
    }
    switch (dir) {
      case Direction.Bottom:
        cx = 0;
        cy = 0.5;
        break;
      case Direction.Left:
        cx = -0.5;
        cy = 0;
        break;
      case Direction.Top:
        cx = 0;
        cy = -0.5;
        break;
      case Direction.Right:
        cx = 0.5;
        cy = 0;
        break;
    }
    return {
      x: this.originPoint.x + this.size.cx * cx,
      y: this.originPoint.y + this.size.cy * cy,
    };
  }

  draw(ctx: CanvasRenderingContext2D, active: boolean = false) {
    this.drawShape(ctx, active);
    this.drawText(ctx, active);
  }

  /**
   *
   * @param ctx 画形状
   * @param active
   */
  abstract drawShape(ctx: CanvasRenderingContext2D, active: boolean);

  /**
   *
   * @param ctx 画文本
   * @param active
   */
  drawText(ctx: CanvasRenderingContext2D, active: boolean) {
    // Text
    ctx.fillStyle = this.colorText;
    ctx.font = this.font;
    let textWidth = ctx.measureText(this.name).width;
    ctx.fillText(
      this.name,
      this.originPoint.x + textWidth * -0.5,
      this.originPoint.y + this.size.cy * -0.5 + 18
    );
  }

  /**
   *
   * @param ctx 检测是否点击到对象
   * @param p
   */
  hit(ctx: CanvasRenderingContext2D, p: Point): boolean {
    let rs =
      Math.abs(p.x - this.originPoint.x) <= this.size.cx * 0.5 &&
      Math.abs(p.y - this.originPoint.y) <= this.size.cy * 0.5;
    return rs;
  }

  /**
   * 移动
   * @param offset
   */
  move(offset: Size) {
    this.originPoint.x += offset.cx;
    this.originPoint.y += offset.cy;
  }
}
