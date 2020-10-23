<template>
	<div>
		<table1 name="order/flow" :extend="FlowMngData">
			<div slot="extend">
				<div class="box" v-if="FlowMngData.flow && FlowMngData.flow.type">
					<div class="box-header with-border bg-gray">
						<h3 class="box-title">流程定义</h3>
					</div>
					<div class="box-body editor-controll">
						<FLowEditor :flow-type="FlowMngData.flow.type" :id="FlowMngData.flow.id" ref="editor" />
					</div>
				</div>
			</div>
		</table1>
		<div class="modal fade flow-create" tabindex="-1" role="dialog">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title">选择流程类型</h4>
					</div>
					<div class="modal-body">
						<component :is="SelectEditor" args="flowType" v-model="flowType" />
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						<button type="button" class="btn btn-primary" @click.prevent="doCreate">确定</button>
					</div>
				</div>
			</div>
		</div>
		<div class="modal fade flow-clone" tabindex="-1" role="dialog">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title">输入新的流程ID</h4>
					</div>
					<div class="modal-body">
						<div class="form-group">
							<input type="text" class="form-control" v-model="cloneId" />
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						<button type="button" class="btn btn-primary" @click.prevent="doClone">确定</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
import { ajax, msg, progress } from "@f/vendor"
import { Table1, editors } from "@f/framework"
import FLowEditor from "./editor"

let FlowMngData = {
	$ui: null,
	flow: {
		type: "",
		id: "",
	},
	formActions: ["save", "clone", "cancel"],
	searchCreate() {
		$("div.flow-create").modal()
	},
	formClone() {
		this.$ui.cloneId = ""
		$("div.flow-clone").modal()
	},
	formEnter() {
		FlowMngData.flow.type = this.form.type
		FlowMngData.flow.id = this.form.id
		this.$data._DataForm.formEnter.apply(this.$data)
	},
	formPostData() {
		try {
			let data = this.$data._DataForm.formPostData.apply(this.$data)
			data.flowData = this.$ui.$refs.editor.getFlowData()
			return data
		} catch (err) {
			console.log(err)
			msg.info(err, "error")
			throw err
		}
	},
}

export default {
	components: { Table1, FLowEditor },
	data() {
		return { FlowMngData, SelectEditor: editors.get("select"), flowType: "", cloneId: "" }
	},
	mounted() {
		FlowMngData.$ui = this
	},
	methods: {
		doCreate() {
			if (!this.flowType) return
			$("div.flow-create").modal("hide")
			FlowMngData.$data.form = { $op: 1, type: this.flowType }
			FlowMngData.$data.formEnter()
		},
		async doClone() {
			if (!this.cloneId) return

			progress.show()
			try {
				let flowData = this.$refs.editor.getFlowData()
				let flow = Object.assign({}, FlowMngData.$data.form)
				flow.id = this.cloneId
				flow.$op = 1

				let url = FlowMngData.$data.meta.url + "/update"
				let rs = await ajax.post({ url, data: { flow: [flow], flowData } })
				if (rs.flow && rs.flow[0]) {
					if (!FlowMngData.$data.data.data) FlowMngData.$data.data.data = []
					FlowMngData.$data.data.data.push(rs.flow[0])
					rs.flow[0].$op = 0
				}
				$("div.flow-clone").modal("hide")
				FlowMngData.$data.formCancel()
			} catch (err) {
				console.log(err)
				msg.info("复制流程出错", "error")
			} finally {
				progress.hide()
			}
		},
	},
}
</script>

<style lang="less" scoped>
.editor-controll {
	display: flex;
	flex-flow: column nowrap;
}
</style>

<i18n-yaml>
cn:
  clone: 复制
en:
  clone: Clone
</i18n-yaml>
