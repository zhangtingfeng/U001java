/**
 * 系统预定义颜色
 */
export enum Colors {
  Text = "#fff",
  Start = "#18A1E2",
  End = "#18A1E2",
  NodeNormal = "#18A1E2",
  NodeNormal2 = "#E6A23C",
  NodeActive = "#F56C6C",

  LinkNormal = "#67C23A",
  LinkActive = "#F56C6C",

  Font = "13px MSYH,Arial",
}
/**
 * 坐标点
 */
export interface Point {
  x: number;
  y: number;
}

/**
 * 尺寸
 */
export interface Size {
  cx: number;
  cy: number;
}

/**
 * 方向
 */
export enum Direction {
  Bottom = 1,
  Left = 2,
  Top = 3,
  Right = 0,
}

/**
 * 矩形
 */
export interface Rect extends Point, Size {}

/**
 * 属性定义
 */
export interface Prop {
  id: String;
  name: String;
}

/**
 * 图形基类
 */
export abstract class Graphic {
  type: String;
  name: string;
  data: any = {};

  /**
   * 画出图形
   * @param ctx
   * @param active
   */
  abstract draw(ctx: CanvasRenderingContext2D, active: boolean): void;
  /**
   * 判断是否命中图形范围
   * @param ctx
   * @param p
   */
  hit(ctx: CanvasRenderingContext2D, p: Point): boolean {
    return false;
  }
  /**
   * 移动
   * @param offset
   */
  move(offset: Size) {}
}

export interface ActiveGraphicObserver {
  (prev: Graphic, curr: Graphic);
}

export interface GraphicsObserver {
  (add: Graphic, remove: Graphic);
}

/**
 * 工具栏
 */
export class Utils {
  /**
   * 画圆角矩形
   * @param ctx
   * @param p 矩形左上角坐标
   * @param size
   * @param radius
   */
  static drawRoundedRect(
    ctx: CanvasRenderingContext2D,
    p: Point,
    size: Size,
    radius
  ) {
    ctx.moveTo(p.x + radius, p.y);
    ctx.arcTo(p.x + size.cx, p.y, p.x + size.cx, p.y + radius, radius); // draw right side and bottom right corner
    ctx.arcTo(
      p.x + size.cx,
      p.y + size.cy,
      p.x + size.cx - radius,
      p.y + size.cy,
      radius
    ); // draw bottom and bottom left corner
    ctx.arcTo(p.x, p.y + size.cy, p.x, p.y + size.cy - radius, radius); // draw left and top left corner
    ctx.arcTo(p.x, p.y, p.x + radius, p.y, radius);
  }

  /**
   * 计算三点的角度
   */
  static angle = (p: Point, p1: Point, p2: Point): number => {
    const x1 = p1.x - p.x;
    const y1 = p1.y - p.y;
    const x2 = p2.x - p.x;
    const y2 = p2.y - p.y;
    const dot = x1 * x2 + y1 * y2;
    const det = x1 * y2 - y1 * x2;
    const angle = (Math.atan2(det, dot) / Math.PI) * 180;
    return (angle + 360) % 360;
  };

  /**
   * 计算两点距离
   */
  static distance = (p1: Point, p2: Point): number => {
    return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
  };
}
