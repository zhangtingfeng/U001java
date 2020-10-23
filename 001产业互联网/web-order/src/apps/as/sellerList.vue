<template>
	<div>
		<div class="order-list">
			<div class="lr-padding">
				<h4>我的售后</h4>
			</div>

			<div class="tabs">
				<a v-for="t of status" :key="t.id" href="#" :class="t.clz" @click="changeStatus(t)">
					<span style="font-weight: bold"> 	{{ t.text }}</span>({{t.recordcount}})   <span class="lineRight" ></span>
				</a>
			</div>
			<div class="lr-padding">
				<form class="search-form">
					<div class="row">
						<div class="col-md-4">
							<label class="control-label">创建时间</label>
							<div class="multi-controls">
								<comp-date v-model="serviceSearchTime.starttime"/>
								<span style="margin:5px 15px 0;">至</span>
								<comp-date v-model="serviceSearchTime.endtime"/>
							</div>
						</div>
						<div class="col-md-4">
							<label class="control-label">&nbsp;</label>
							<div class="multi-controls">
								<button class="btn btn-search" @click.prevent="doQuery"><i class="fa fa-search"></i>搜索</button>
							</div>
						</div>
					</div>
				</form>
			</div>
			<hr />
			<div class="lr-padding">
				<template v-for="(service, i) in services">
					<div class="order-item" :key="service.id" v-if="service.state2 == state || state == '0'">
						<div class="oneListline">
							<div class="headerline">
								<span>序号 ：{{ services.length- i }}</span>
								<span>售后编号: {{(service.processid)}}</span>
								<span>买家状态 : {{ buyerStatus(service.state1) }}</span>
								<span>卖家状态 : {{ sellerStatus(service.state2) }}</span>
								<span>解决类型 : {{ (service.selleraftermarkettype) }}</span>


							</div>
							<div class="dateailline">
								<span>提交时间 : {{ service.createTime }}</span>
								<span>订单编号 : {{ service.orderId }}</span>
								<span>买家机构 : {{ service.buyers ? service.buyers.orgName : "" }}</span>
								<span>买家电话 : {{ service.buyers ? service.buyers.mobile : "" }}</span>
							</div>
							<div class="buttonline">
								<a class="btn whitebutton" :href="service.href">查看详情
								</a>
							</div>
						</div>


					</div>
				</template>
				<template v-if="!services">
					<p style="text-align:center;font-size:16px;">暂时没有售后信息</p>
				</template>
			</div>
		</div>
	</div>
</template>

<script>
import { msg, ajax } from "@f/vendor"
import { comps, dict } from "@f/framework"
import utils  from '@/common/util';
//import { getUrlKey } from '@/utils';

export default {
	components: {
		CompDate: comps.Date,
	},
	data() {
		return {
			orders: [],
			status: [],
			state: "0",
			data1: [],
			data2: [],serviceSearchTime:{},services:[],

		}
	}, //离开当前页面后执行  不要缓存
	destroyed: function () {
		location.reload();
	},
	methods: {
		async init() {
			//return;
			//let path  =
			//debugger;
			//this.buyerORseller = utils.getUrlKey('id',window.location.href);
			//this.serviceSearchTime.starttime=new Date((new Date).getFullYear(),(new Date).getMonth(),(new Date).getDate()-30);
			//this.serviceSearchTime.endtime=new Date();

		//	let startTime1 = new Date(new Date(new Date().toLocaleDateString()).getTime()); // 当天0点
	///		let endTime1 = new Date(new Date(new Date().toLocaleDateString()).getTime() +24 * 60 * 60 * 1000 -1);//

		//	this.serviceSearchTime.starttime=new Date();
		//	this.serviceSearchTime.endtime=new Date();

			this.serviceSearchTime.buyerOrSeller="seller";

			let data1 = await dict.get("buyerAftermarketStatus");
			let data2 = await dict.get("sellerAftermarketStatus");
			this.data1 = data1;
			this.data2 = data2;
			var status = [];
			status.push({ id: "0", text: "全部", clz: "active",recordcount:0 });
			for (var d of data2) {
				d.recordcount=0;
				status.push(d);
			}
			this.status = status;


			//this.buyerORseller = this.$route.query.i999d;
			//this.buyerORseller =getUrlKey("i999d ",window.location.href) //分割url
			await this.iniQuery(this.serviceSearchTime);
			//return;

		//	await this.iniQuery();

		},
		async iniQuery(orderSearchTime){
			try {


				let data = await ajax.post({
					url: "api/order/aftermarketinfo/getServicesList?buyerOrSeller=seller",
					data: orderSearchTime,
				})

				if (data.services) {
                   debugger;
					if (data.services.length==0){
                        this.services=[];
						msg.info("暂无售后");
					}
					else {
						for (var service of data.services) {
                            service.href = "#!m/order/as?id=" + service.processid;
							for (var s of data.buyers) {
								if (service.buyerId == s.orgId) {
									service.buyers = s
									break
								}
							}
							let ddddata = this.data2;
							this.status[0].recordcount++;
							for(let i=0;i<ddddata.length;i++){
								//debugger;
								if (ddddata[i].id==service.state2){
									this.status[i+1].recordcount++;
								}
							}
						}
						this.services = data.services.reverse();
					}

				}
				else{
                    this.services=[];
                }
			} catch {
				msg.info("获取售后列表失败")
			}
		},
		doQuery() {
			this.iniQuery(this.serviceSearchTime);
		},
		buyerStatus(val) {
			for (var d of this.data1) {
				if (d.id == val) {
					return d.text
				}
			}
			return "完成"
		},
		sellerStatus(val) {
			for (var d of this.data2) {
				if (d.id == val) {
					return d.text
				}
			}
			return "完成"
		},
		changeStatus(t) {
			for (var m of this.status) {
				m.clz = ""
			}
			t.clz = "active"
			this.state = t.id
		},
	},
	beforeMount() {
		this.init();
	},
}
</script>
<style lang="less" scoped>
@import url("@/common/common.less");

.order-list {
	background-color: #ffffff;  padding-bottom: 5px;
}
.lr-padding {
	margin: auto 20px;
}
.order-info {
	position: relative;
	.order-amount {
		position: absolute;
		right: 30px;
		bottom: -10px;
	}
	span {
		margin: 0 10px;
	}
}
hr {
	border-color: @bg1;
	margin-left: 0;
	margin-right: 0;
}
.multi-controls {
	display: flex;
	flex-flow: row nowrap;

	input {
		flex: 1;
	}
	span {
		flex: 0;
	}
}

.search-form {
	.btn-search {
		background-color: @color1;
		border-color: @color1;
		color: @text2;
		padding-left: 30px;
		padding-right: 30px;
	}
}

.order-item {
	border: 1px solid @bg1;
	margin-bottom: 10px;
	.fee {
		font-size: 25px;
		color: @color1;
	}
	.detail {
		padding-bottom: 20px;
		.goods {
			flex: 1;
			display: flex;
			flex-flow: row nowrap;
			align-items: center;

			img {
				width: 100px;
				height: 100px;
			}

			.props {
				display: flex;
				flex-flow: row wrap;

				p {
					width: 200px;
				}
			}
			.my-btn {
				color: #6abf4b;
				background-color: #ffffff;
				border-color: #6abf4b;
				width: 100px;
			}
		}
	}
}
</style>
