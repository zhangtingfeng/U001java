import { Colors, Graphic, Point, Size, Utils, Direction } from "./Common"
import Node from "./Node"

/**
 * 连线类
 */
export default class Link extends Graphic {
	static ArrawLen = 5
	static HitRange = 5

	public colorNormal = Colors.LinkNormal
	public colorActive = Colors.LinkActive

	constructor(private n1: Node, private n2: Node) {
		super()
		this.type = "link"
		if (!n1.canLinkFrom || !n2.canLinkTo) throw `Can't create link [${n1.name} => ${n2.name}]`
	}

	public toString = () => {
		return `Link: ${this.n1} = ${this.n2}`
	}

	/**
	 * 连线起始节点
	 */
	get node1(): Node {
		return this.n1
	}

	/**
	 * 连线结束节点
	 */
	get node2(): Node {
		return this.n2
	}

	hit(ctx: CanvasRenderingContext2D, p: Point): boolean {
		let points = this.caclLinkPoint()
		let rs = Utils.angle(p, points[0], points[1])
		console.log(rs)
		return Math.abs(180 - rs) < Link.HitRange
	}

	caclLinkPoint(): Point[] {
		const angle = Utils.angle(
			this.n1.originPoint,
			{
				x: this.n1.originPoint.x + 1,
				y: this.n1.originPoint.y,
			},
			this.n2.originPoint
		)
		// 45 ~ 135, 135 ~ 225, 225 ~ 315, 315 ~ 405(45)
		// const direction = (Math.floor((angle + 45) / 90) % 4) as Direction;
		let direction = Direction.Right
		if (angle >= 30 && angle <= 150) direction = Direction.Bottom
		else if (angle > 150 && angle <= 210) direction = Direction.Left
		else if (angle > 210 && angle <= 330) direction = Direction.Top
		console.log(direction, (direction + 2) % 4)
		return [this.n1.getLinkPoint(direction), this.n2.getLinkPoint((direction + 2) % 4)]
	}

	draw(ctx: CanvasRenderingContext2D, active: boolean): void {
		ctx.fillStyle = ctx.strokeStyle = active ? this.colorActive : this.colorNormal

		// line
		let points: Point[] = this.caclLinkPoint()
		ctx.beginPath()
		ctx.moveTo(points[0].x, points[0].y)
		ctx.lineTo(points[1].x, points[1].y)
		ctx.closePath()
		ctx.stroke()

		let drawArrowhead = (ctx, x, y, radians) => {
			ctx.save()
			ctx.beginPath()
			ctx.translate(x, y)
			ctx.rotate(radians)
			ctx.moveTo(0, 0)
			ctx.lineTo(Link.ArrawLen, Link.ArrawLen * 1.5)
			ctx.lineTo(-Link.ArrawLen, Link.ArrawLen * 1.5)
			ctx.closePath()
			ctx.restore()
			ctx.fill()
		}

		// Arrow
		// var startRadians = Math.atan(
		//   (points[1].y - points[0].y) / (points[1].x - points[0].x)
		// );
		// startRadians += ((points[1].x > points[0].x ? -90 : 90) * Math.PI) / 180;
		// drawArrowhead(ctx, points[0].x, points[0].y, startRadians);
		// draw the ending arrowhead
		var endRadians = Math.atan((points[1].y - points[0].y) / (points[1].x - points[0].x))
		endRadians += ((points[1].x > points[0].x ? 90 : -90) * Math.PI) / 180
		drawArrowhead(ctx, points[1].x, points[1].y, endRadians)
	}
}
