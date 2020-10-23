<template>
	<div class="box collapsed-box boxbackgroundcolor">
		<div class="box-header width-border bg-gray">
			<h3 class="box-title">基本信息</h3>
			<div class="box-tools pull-right">
				<button type="button" class="btn btn-box-tool" data-widget="collapse">
					<i class="fa fa-plus"></i>
				</button>
			</div>
		</div>
		<div class="box-body">
			<p>订单编号: {{ id }}</p>
			<p>流程节点: {{ base.node1 }}</p>
			<p>创建时间: {{ order.createTime}}</p>
			args={{args}}

		</div>
	</div>
</template>

<script>
export default {
	data() {
		return {
			total: 0,
			order: this.orderFulldetail.order,
		}
	},
	props: ["view", "id", "readonly", "args", "base","orderFulldetail"],
}
</script>
<style lang="less" scoped>
	@import url("@/common/common.less");
</style>
