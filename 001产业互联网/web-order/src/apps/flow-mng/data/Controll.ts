import { Point, Size, Graphic } from "./Common";
import Repository from "./Repository";
import Link from "./Link";
import Node from "./Node";
import ActivityNode from "./ActivityNode";
/**
 * 流程操作控制
 */
export default class Controll {
  selected: Graphic = null;
  mousePrevPoint: Point;
  linking: Link = null;
  mouseDown: Boolean = false;

  constructor(
    private ctx: CanvasRenderingContext2D,
    private repo: Repository,
    private controllAble: boolean = true
  ) {
    let canvas = ctx.canvas;
    canvas.addEventListener("mousedown", (event) =>
      this.doMouseDown({ x: event.offsetX, y: event.offsetY }, event)
    );
    canvas.addEventListener("mousemove", (event) =>
      this.doMouseMove({ x: event.offsetX, y: event.offsetY }, event)
    );
    canvas.addEventListener("mouseup", (event) =>
      this.doMouseUp({ x: event.offsetX, y: event.offsetY }, event)
    );
    canvas.addEventListener("keydown", (event) => this.doKeydown(event));
  }

  log(p: Point) {
    console.log(
      `point:${p.x}, ${p.y};Selected:${this.selected}, active: ${this.repo.active}, linking:${this.linking}`
    );
  }

  /**
   * 自动对齐所有图形
   */
  autoAlign() {
    const UNIT = 50.0;
    for (let g of this.repo.graphics) {
      if (g instanceof Node) {
        let n = g as Node;
        n.originPoint.x = Math.floor(n.originPoint.x / UNIT + 0.5) * UNIT;
        n.originPoint.y = Math.floor(n.originPoint.y / UNIT + 0.5) * UNIT;
      }
    }
    this.repo.update(null);
  }

  hitGraphic(p: Point): Graphic {
    let rs: Graphic = null;
    let gs: Graphic[] = this.repo.graphics;
    for (let i = gs.length - 1; i >= 0; i--) {
      // for (let g of this.repo.graphics) {
      let g = gs[i];
      if (g.hit(this.ctx, p)) {
        rs = g;
        break;
      }
    }
    return rs;
  }

  move(dist: Size) {
    for (let g of this.repo.graphics) {
      if (g instanceof Node) {
        let n: Node = g as Node;
        n.originPoint.x += dist.cx;
        n.originPoint.y += dist.cy;
      }
    }
  }

  doKeydown(event: KeyboardEvent): any {
    // delete
    if (event.keyCode === 0x2e) {
      if (this.repo.active != null) {
        this.repo.remove(this.repo.active);
      }
    }
  }

  doMouseDown(p: Point, event: MouseEvent): void {
    this.mouseDown = true;
    let g: Graphic = this.hitGraphic(p);

    if (this.controllAble && g !== null && g instanceof Node) {
      let n: Node = g as Node;
      if (event.ctrlKey) {
        this.linking = new Link(
          n,
          new ActivityNode("Linking", p, { cx: 0, cy: 0 })
        );
      } else {
        this.selected = g;
      }
    }
    this.repo.active = g;
    this.mousePrevPoint = p;
  }
  doMouseMove(p: Point, event: MouseEvent) {
    // 画连线
    if (this.linking !== null) {
      this.repo.update(null);
      this.linking.node2.move({
        cx: p.x - this.mousePrevPoint.x,
        cy: p.y - this.mousePrevPoint.y,
      });
      this.linking.draw(this.ctx, true);
    }
    // 选中了图形
    else if (this.selected) {
      this.selected.move({
        cx: p.x - this.mousePrevPoint.x,
        cy: p.y - this.mousePrevPoint.y,
      });
      this.repo.update(this.selected);
    }
    // 当前什么都没有选中
    else {
      // 如果按下鼠标左键，则这个图形平移
      if (this.mouseDown) {
        let dist: Size = {
          cx: p.x - this.mousePrevPoint.x,
          cy: p.y - this.mousePrevPoint.y,
        };
        this.move(dist);
        this.repo.update(null);
      }
    }
    this.mousePrevPoint = p;
  }
  doMouseUp(p: Point, event: MouseEvent) {
    this.mouseDown = false;
    if (this.linking !== null) {
      let g: Graphic = this.hitGraphic(p);
      if (g !== null && g instanceof Node) {
        try {
          this.repo.add(new Link(this.linking.node1, g as Node));
        } catch (e) {
          this.repo.update(null);
        }
      } else {
        this.repo.update(null);
      }

      this.linking = null;
    }
    this.selected = null;
  }
}
