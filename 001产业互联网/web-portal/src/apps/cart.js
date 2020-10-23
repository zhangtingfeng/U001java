import { ajax } from "@f/vendor"

export default {
	cartNums: 0,
	async init() {
		let data = await ajax.post({
			url: "api/portal/cart/getCartNums",
		})
		if (data.cartNums) {
			this.cartNums = data.cartNums
		}
		return this
	},
}
