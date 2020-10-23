<template>
	<div class="container main-info">
		<div class="row">
			<div class="col-md-8">
				<div class="info-panel container-fluid">
					<div class="header row">
						<div class="col-md-6">
							<div class="info-content">
								<h3><img src="imgs/main/icon_01.png" /> 苹果价格折线图</h3>
							</div>
						</div>
						<div class="col-md-6">
							<div class="info-content">
								<h3><img src="imgs/main/icon_02.png" /> 纸袋富士80#</h3>
							</div>
						</div>
					</div>
					<div class="body row">
						<div class="info-content">
							<div class="col-md-6 info-chart">
								<h4>7月份数据</h4>
								<img class="img-responsive" src="imgs/main/pic_02.png" />
								<p>纵轴：单位，元/斤<span class="pull-right">横轴：日期</span></p>
							</div>
							<div class="col-md-6">
								<table class="table text-center">
									<thead>
										<tr>
											<th>日期</th>
											<th>栖霞</th>
											<th>蓬莱</th>
											<th>陕西</th>
											<th>甘肃</th>
										</tr>
									</thead>
									<tbody>
										<tr v-for="i of 8" :key="i">
											<td>{{ dt.m }}.{{ dt.d }}</td>
											<td>1.93</td>
											<td>2.50</td>
											<td>2.95</td>
											<td>3.58</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
				<div class="info-panel">
					<div class="header">
						<div class="info-content">
							<h3><img src="imgs/main/icon_04.png" /> 农肥价格折线图</h3>
						</div>
					</div>
					<div class="body row">
						<div class="info-content">
							<div class="col-md-6 info-chart">
								<h4>7月份数据</h4>
								<img class="img-responsive" src="imgs/main/pic_03.png" />
								<p>纵轴：单位，元/斤<span class="pull-right">横轴：日期</span></p>
							</div>
							<div class="col-md-6">
								<table class="table text-center">
									<thead>
										<tr>
											<th>日期</th>
											<th>尿素</th>
											<th>硝酸二胺</th>
											<th>硫酸钾</th>
											<th>毒死蜱</th>
										</tr>
									</thead>
									<tbody>
										<tr v-for="i of 8" :key="i">
											<td>{{ dt.m }}.{{ dt.d }}</td>
											<td>1630.00</td>
											<td>2325.00</td>
											<td>2550.00</td>
											<td>41000.00</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-4">
				<div class="info-panel">
					<div class="header">
						<div class="info-content">
							<h3><img src="imgs/main/icon_03.png" /> 主要自然灾害预测</h3>
						</div>
					</div>
					<div class="header">
						<div class="info-content">更新时间：2020.09.18</div>
					</div>

					<div class="zaihai-info" v-for="i of 4" :key="`zaihai${i}`">
						<div class="info-content">
							<img :src="`imgs/main/icon_zaihai${i}.png`" />
							<div>
								<p>{{ zaihai[i - 1] }}</p>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-8">
				<div class="info-panel">
					<div class="header">
						<div class="info-content">
							<h3><img src="imgs/main/icon_05.png" /> 实时价格</h3>
						</div>
					</div>
					<div class="body">
						<div class="info-content">
							<table class="table text-center">
								<tbody>
									<tr v-for="i of 6" :key="i">
										<td width="24px"><img src="imgs/main/icon_ring.png" /></td>
										<td>品种</td>
										<td>品级</td>
										<td>产地价格</td>
										<td>全国均价</td>
										<td>最高价</td>
										<td>产地</td>
										<td>{{ dt.y }}.{{ dt.m }}.{{ dt.d }}</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-4">
				<div class="info-panel">
					<img class="img-responsive" src="imgs/main/pic_01.png" />
				</div>
			</div>
		</div>

		<div class="zixun-banner">
			<div class="line">&nbsp;</div>
			<h3>行业资讯</h3>
			<div class="line">&nbsp;</div>
		</div>

		<div class="row">
			<div class="col-md-4" v-for="i of 3" :key="i">
				<div class="zixun-info ">
					<img class="img-responsive" :src="`imgs/main/zixun_pic_${i}.png`" />
					<div class="dt"><img src="imgs/main/icon_date.png" /> {{ zixun[i - 1].dt }}</div>
					<div class="info-content">
						<div class="title">{{ zixun[i - 1].title }}</div>
						<div class="text">{{ zixun[i - 1].content }}</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
const ZAIHAI = [
	"呼伦贝尔市气象台2020年9月18日10时06分发布霜冻蓝色预警信号：48小时内海拉尔区、陈巴尔虎旗、鄂温克族自治旗、，满洲里市、新巴尔虎右旗、新巴尔虎左旗，牙克石市、额尔古纳市、根河市、鄂伦春自治旗、扎兰屯市山区、阿荣旗山区和莫力达瓦达斡尔族自治旗山区地面最低温度将要下降到0℃以下，出现霜，冻对农业将产生影响。请有关单位和人员做好防范准备",
	"泉州市气象台2020年09月18日15时00分变更发布暴雨红色预警信号：目前泉港区局部乡镇已经暴雨，预计3小时内降雨量将达100毫米以上。请注意防范！",
	"塔城地区气象台2020年8月4日16时34分发布冰雹橙色预警信号：目前塔城地区北部雷达回波较强，预计未来6小时内塔城地区北部可能出现冰雹天气，并造成雹灾，请注意加强防范。",
	"威海市气象台8月4日16时00分发布海上大风黄色预警信号：受台风”黑格比”影响，今天后半夜到明天夜间，我市阴有中雨，荣成大雨，东部、南部海区南风6~-7级明天白天转东南风7~8级阵风10级，内陆南风明天白天转东南风5~6级阵风7级。请注意防范",
]

const ZIXUN = [
	{
		dt: "2020-07-14 08:46",
		title: "新一季苹果产量或与去年持平",
		content: "中果网讯受新冠疫情影响，水果的进出口行情出现一定程度的起伏波动，就过程苹果而言，统计显示出口保持.....",
	},
	{
		dt: "2020-07-14 08:46",
		title: "束怀瑞院士谈中国果树产业可持续发展战略",
		content:
			"束怀瑞(1929.9.26 -)，果树学专家，山东省淄博市人，山东农业大学教授、博导，中国园艺学会常务理事。1950年毕业于山东农学院.....",
	},
	{
		dt: "2020-07-14 08:46",
		title: "山东省人民政府办公厅关于印发山东省推动苹果产业高质量发展行动计划的通知",
		content: "为深入实施乡村振兴战略大力推进农业供给侧结构性改革，充分发挥我省苹果产业传统优势.....",
	},
]
export default {
	data() {
		let dt = new Date()
		return {
			dt: {
				y: dt.getFullYear(),
				m: dt.getMonth() + 1,
				d: dt.getDate(),
			},
			zaihai: ZAIHAI,
			zixun: ZIXUN,
		}
	},
}
</script>

<style lang="less" scoped>
@border: 1px solid #ccc;

.info-panel {
	margin-top: 20px;
	border: @border;
	.header {
		border-bottom: @border;

		img {
			margin-right: 10px;
		}
	}

	.info-content {
		margin: 10px 20px;
	}

	.info-chart {
		h4 {
			margin: 20px auto;
		}
		p {
			margin: 18px auto;
		}
	}

	.zaihai-info {
		border-bottom: @border;
		&:last-child {
			border-bottom: none;
		}

		.info-content {
			display: flex;
			flex-flow: row nowrap;
			align-items: center;
			img {
				flex: 0;
				margin-right: 20px;
			}
			div {
				flex: 1;
			}
		}
	}
}

.zixun-banner {
	display: flex;
	flex-flow: row nowrap;
	align-items: center;
	h3 {
		flex: 0;
		text-align: center;
		margin: 40px 10px;
		min-width: 150px;
	}
	div.line {
		flex: 1;
		height: 1px;
		border-top: @border;
	}
}

.zixun-info {
	border: @border;
	position: relative;
	margin-bottom: 20px;

	.dt {
		padding-left: 10px;
		padding-right: 20px;
		img {
			height: 18px;
			position: relative;
			top: -1px;
		}
		position: absolute;
		top: 160px;
		color: #fff;
		background-color: #007cff;
		line-height: 18px;

		border-radius: 0 10px 10px 0;
	}

	.info-content {
		margin: 20px;

		.title {
			font-weight: bolder;
			margin: 5px auto;
		}
	}
}
</style>
