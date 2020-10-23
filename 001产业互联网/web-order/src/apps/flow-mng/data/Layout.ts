import { Point, Size, Graphic } from "./Common";
import Repository from "./Repository";

/**
 * 流程输出
 */
export default class Layout {
  constructor(private ctx: CanvasRenderingContext2D, private repo: Repository) {
    repo.subscribActive((prev, curr) => {
      this.drawAll();
    });
    repo.subscribGraphics((add, remove) => {
      this.drawAll();
    });
  }

  /**
   * 重画所有
   */
  drawAll() {
    this.ctx.clearRect(0, 0, this.ctx.canvas.width, this.ctx.canvas.height);
    this.draw(this.repo.graphics);
  }

  /**
   * 重画指定图形
   * @param graphics
   */
  draw(graphics: Graphic[]) {
    let a = this.repo.active;
    for (let g of graphics) g.draw(this.ctx, g === a);
  }
}
