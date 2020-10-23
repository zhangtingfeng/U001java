<template>
	<div>
		<div class="box-header box-solid bg-gray">
			<h3 class="box-title">费用审批流程</h3>
		</div>
		<div class="box-body">
			<p>标题: {{ fee.title }}</p>
			<p>计划在 {{ fee.paymentDt }} 支付 ￥{{ fee.amount }}</p>
			<p>收款方: {{ fee.receiptOrg }}</p>
		</div>
	</div>
</template>

<script>
import { ajax } from "@f/vendor"

export default {
	props: ["process"],
	data() {
		return {
			fee: {},
		}
	},
	mounted() {
		this.init()
	},
	methods: {
		async init() {
			let datas = await ajax.post({
				url: "api/order/meta/fee-process/query",
				data: { id: this.process.serviceId },
			})
			this.fee = datas["fee-process"][0]
		},
	},
}
</script>
