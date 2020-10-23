import { ActiveGraphicObserver, GraphicsObserver } from "./Common";
import { Graphic } from "./Common";
import Link from "./Link";
import Node from "./Node";

/**
 * 流程图存储对象
 */
export default class Repository {
  private _graphics: Graphic[] = [];
  private _activeGraphic: Graphic = null;
  private _activeObservers: ActiveGraphicObserver[] = [];
  private _graphicsObservers: GraphicsObserver[] = [];

  /**
   * 获取所有图形对象
   */
  get graphics() {
    return this._graphics;
  }
  /**
   * 当前激活对象
   */
  get active() {
    return this._activeGraphic;
  }
  set active(value) {
    let old = this._activeGraphic;
    this._activeGraphic = value;
    this.updateActives(old, this._activeGraphic);
  }
  /**
   * 订阅active变更
   * @param observer
   */
  public subscribActive(observer: ActiveGraphicObserver) {
    this._activeObservers.push(observer);
  }
  /**
   * 取消active变更订阅
   * @param observer
   */
  public unsubscribActive(observer: ActiveGraphicObserver) {
    let idx = this._activeObservers.indexOf(observer);
    if (idx > -1) this._activeObservers.splice(idx, 1);
  }
  /**
   * 触发active订阅事件
   * @param prev
   * @param curr
   */
  private updateActives(prev: Graphic, curr: Graphic) {
    for (let ob of this._activeObservers) ob(prev, curr);
  }

  /**
   *
   * @param n1 获取两个节点之间的连线
   * @param n2
   */
  getLink(n1: Node, n2: Node): Link {
    for (let g of this._graphics) {
      if (g instanceof Link) {
        let l: Link = g as Link;
        if (
          (l.node1 === n1 && l.node2 === n2) ||
          (l.node1 === n2 && l.node2 === n1)
        )
          return l;
      }
    }
    return null;
  }

  /**
   * 获取节点对应的所有连线
   * @param node
   */
  getLinks(node: Node): Link[] {
    let rs: Link[] = [];
    for (let g of this._graphics) {
      if (g instanceof Link) {
        let l: Link = g as Link;
        if (l.node1 === node || l.node2 === node) rs.push(l);
      }
    }
    return rs;
  }

  /**
   *
   * @param g 增加一个对象
   */
  add(g: Graphic) {
    if (g instanceof Link) {
      let l: Link = g as Link;
      if (l.node1 === l.node2) throw "Can't link to self";
      if (null !== this.getLink(l.node1, l.node2)) throw `${l} has exists`;
    }
    this._graphics.push(g);
    this.updateGraphics(g, null);
  }

  /**
   * 删除一个对象
   * @param g
   */
  remove(g: Graphic) {
    let idx = this._graphics.indexOf(g);
    if (idx > -1) {
      this._graphics.splice(idx, 1);
      if (g instanceof Node) {
        let links: Link[] = this.getLinks(g as Node);
        for (let l of links) {
          this.remove(l);
        }
      }
      this.updateGraphics(null, g);
    }
  }
  /**
   * 更新一个对象
   * @param g
   */
  update(g: Graphic) {
    this.updateGraphics(g, g);
  }

  /**
   * 订阅图形变更事件
   * @param observer
   */
  public subscribGraphics(observer: GraphicsObserver) {
    this._graphicsObservers.push(observer);
  }
  /**
   * 取消订阅图形变更事件
   * @param observer
   */
  public unsubscribGraphics(observer: GraphicsObserver) {
    let idx = this._graphicsObservers.indexOf(observer);
    if (idx > -1) this._graphicsObservers.splice(idx, 1);
  }
  /**
   * 触发图形变更事件
   * @param add
   * @param remove
   */
  private updateGraphics(add: Graphic, remove: Graphic) {
    for (let ob of this._graphicsObservers) ob(add, remove);
  }
}
