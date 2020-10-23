<template>
    <div class="flow-editor">
        <div class="toolbar btn-toolbar">
            <div class="btn-group">
                <!-- <a type="button" class="btn btn-default" @click.prevent="doSave()">
          <i class="fa fa-save"></i> 保存
        </a>-->
                <a type="button" class="btn btn-default" @click.prevent="doAlign()">
                    <i class="fa fa-align-justify"></i> 对齐
                </a>
                <a type="button" class="btn btn-default" @click.prevent="doDel()" :disabled="curr.t === 0">
                    <i class="fa fa-times"></i> 删除
                </a>
            </div>
            <div class="btn-group">
                <a type="button" class="btn btn-default" @click.prevent="doAddNode(0)">
                    <i class="fa fa-circle"></i> 开始节点
                </a>
                <a type="button" class="btn btn-default" @click.prevent="doAddNode(2)">
                    <i class="fa fa-square"></i> 处理节点
                </a>
                <a type="button" class="btn btn-default" @click.prevent="doAddNode(1)">
                    <i class="fa fa-circle"></i> 结束节点
                </a>
            </div>
        </div>
        <div class="editor">
            <canvas></canvas>
        </div>
        <div class="props" :class="{ 'props-collapse': !showProps }">
            <!-- 流程 -->
            <template v-if="curr.t == 0">
                <div class="box">
                    <div class="box-header bg-gray">
                        <h3 class="box-title">流程变量</h3>
                        <div class="box-tools pull-right">
                            <a href="#" class="btn btn-box-tool" @click.prevent="showProps = !showProps">
                                <i class="fa fa-minus" v-if="showProps"></i>
                                <i class="fa fa-plus" v-else></i>
                            </a>
                        </div>
                    </div>
                    <template v-if="showProps">
                        <flow-props :props="data.props"/>
                    </template>
                </div>
            </template>
            <!-- 节点 -->
            <template v-if="curr.t == 1">
                <div class="box">
                    <div class="box-header bg-gray">
                        <h3 class="box-title">节点设置</h3>
                        <div class="box-tools pull-right">
                            <a href="#" class="btn btn-box-tool" @click.prevent="showProps = !showProps">
                                <i class="fa fa-minus" v-if="showProps"></i>
                                <i class="fa fa-plus" v-else></i>
                            </a>
                        </div>
                    </div>
                    <template v-if="showProps">
                        <node-props :props="data.nodeProps1" @change2="doChange1"/>
                        <div class="box-header bg-gray">
                            <h3 class="box-title">节点属性</h3>
                        </div>
                        <node-props :props="data.nodeProps2" @change2="doChange2"/>
                    </template>
                </div>
            </template>
            <!-- Link -->
            <template v-if="curr.t == 2">
                <div class="box">
                    <div class="box-header bg-gray">
                        <h3 class="box-title">流转设置</h3>
                        <div class="box-tools pull-right">
                            <a href="#" class="btn btn-box-tool" @click.prevent="showProps = !showProps">
                                <i class="fa fa-minus" v-if="showProps"></i>
                                <i class="fa fa-plus" v-else></i>
                            </a>
                        </div>
                    </div>
                    <template v-if="showProps">
                        <node-props :props="data.linkProps" @change2="doChange1"/>
                        <div class="box-header bg-gray">
                            <h3 class="box-title">条件设置</h3>
                        </div>
                        <div class="box-body">
                            <textarea v-model="curr.obj.data.condition" class="form-control" rows="5"/>
                        </div>
                    </template>
                </div>
            </template>
        </div>
    </div>
</template>

<script>
    import {ajax, msg, progress} from "@f/vendor"
    import {dict} from "@f/framework"

    import Layout from "./data/Layout"
    import {Point, Size} from "./data/Common"
    import Node from "./data/Node"
    import Link from "./data/Link"
    import ActivityNode from "./data/ActivityNode"
    import JudgeNode from "./data/JudgeNode"
    import StartNode from "./data/StartNode"
    import EndNode from "./data/EndNode"
    import Controll from "./data/Controll"
    import Repository from "./data/Repository"

    import FlowProps from "./flowProps"
    import NodeProps from "./nodeProps"

    import FlowData from "./FlowData"

    export default {
        components: {FlowProps, NodeProps},
        props: ["flowType", "id"],
        data() {
            return {
                config: {
                    type: "order",
                    nodeProps: {},
                },
                showProps: true,
                count: 0,
                curr: {t: 0, obj: null, props: null},
                flow: {repo: null, layout: null, ctl: null},
                data: {
                    props: [],
                    nodeProps1: [],
                    nodeProps2: [],
                    linkProps: [],
                },
            }
        },
        mounted() {
            this.$nextTick(() => {
                this.init()
            })
        },
        methods: {
            async init() {
                this.resizeCanvas()
                this.initEditor()
                try {
                    progress.show()
                    await this.loadFlowType()
                    await this.loadFlow()
                } catch (err) {
                    console.error(err)
                    msg.info("加载流程数据出错", "error")
                } finally {
                    progress.hide()
                }
            },
            async loadFlowType() {
            	//debugger;
                this.config.type = this.flowType
                let flowTypes = await dict.getObjs("flowType")
                if (flowTypes[this.flowType]) {
                    this.config.linkType = flowTypes[this.flowType].linkType
                }
                let data = await ajax.get(`api/order/flow-mng/type/${this.config.type}`)
                this.config.nodeProps = data.nodeProps
            },
            async loadFlow() {
                if (this.id) {
                    let data = await ajax.get(`api/order/flow-mng/flow/${this.id}`)
                    FlowData.loadFlow(this.flow.repo, data.flow)
                    this.data.props = data.props || []
                }
            },
            resizeCanvas() {
                let $editor = this.$el.querySelector("div.editor")
                let $canvas = $editor.querySelector("canvas")
                $canvas.width = $editor.scrollWidth
                $canvas.height = $editor.scrollHeight
            },
            initEditor() {
                let ctx = this.$el.querySelector("canvas").getContext("2d")
                let repo = new Repository()
                let layout = new Layout(ctx, repo)
                let ctl = new Controll(ctx, repo)
                this.flow = {repo, layout, ctl}
                repo.subscribActive((prev, curr) => this.doShowActive(prev, curr))
            },
            doAlign() {
                this.flow.ctl.autoAlign()
            },
            doAddNode(t) {
                let node
                let id = this.count++
                if (t === 0) {
                    node = new StartNode({x: 120, y: 20}, FlowData.DefaultSize)
                } else if (t === 1) {
                    node = new EndNode({x: 120, y: 20}, FlowData.DefaultSize)
                } else {
                    node = new ActivityNode(`Activity ${id}`, {x: 120, y: 20}, FlowData.DefaultSize)
                }
                node.data = {
                    name: node.name,
                    type: `${t}`,
                    node,
                    links: [],
                    props: {},
                }

                this.flow.repo.add(node)
                return node
            },
            doDel() {
                if (this.flow.repo.active) this.flow.repo.remove(this.flow.repo.active)
            },
            doShowActive(prev, curr) {
                if (prev === curr) return
                if (null === curr) {
                    this.curr = {t: 0}
                } else if (curr.type === "node") {
                    this.curr = {
                        t: 1,
                        obj: curr,
                    }
                    this.updateNodeProps()
                } else if (curr.type === "link") {
                    this.curr = {
                        t: 2,
                        obj: curr,
                    }
                    this.updateLinkProps()
                }
            },
            updateNodeProps() {
                this.data.nodeProps1 = [
                    {id: "id", name: "ID", value: this.curr.obj.data.id},
                    {id: "name", name: "名称", value: this.curr.obj.name},
                ];
			//	debugger;
                let nodeProps2 = []
                for (let n of this.config.nodeProps) {
                    let editor = n.editor ? n.editor.split(".") : []
                    nodeProps2.push({
                        id: n.id,
                        name: n.name,
                        editor: editor[0],
                        editorArgs: editor[1],
                        ext: n.ext,
                        value: this.curr.obj.data.props[n.id],
                    })
                }
                this.data.nodeProps2 = nodeProps2
            },
            updateLinkProps() {
                let editor = this.config.linkType ? this.config.linkType.split(".") : []
                this.data.linkProps = [
                    {id: "name", name: "名称", value: this.curr.obj.name},
                    {id: "type", name: "类型", value: this.curr.obj.data.type, editor: editor[0], editorArgs: editor[1]},
                ]
            },
            doChange1(prop) {
                let node = this.curr.obj
                node.data[prop.id] = prop.value
                if (prop.id === "name") {
                    node[prop.id] = prop.value
                    this.flow.layout.drawAll()
                }
            },
            doChange2(prop) {
                let nodeProps = this.curr.obj.data.props
                nodeProps[prop.id] = prop.value
            },
            // doSave() {
            // 	let flow = FlowData.getFlow(this.flow.repo)
            // 	flow.props = this.data.props
            // 	console.log(flow)
            // },
            getFlowData() {
                let flow = FlowData.getFlow(this.flow.repo)
                for (let p of this.data.props) {
                    if (!p.id) throw `流程变量的名称不能为空，也不能重复，请修改配置`
                }
                flow.props = this.data.props
                return flow
            },
        },
    }
</script>

<style lang="less" scoped>
    .flow-editor {
        position: relative;
        flex: 1 0 auto;
        min-height: 500px;

        .loading {
            animation: progress-modal-icon 2s infinite linear;
        }

        .toolbar {
            position: absolute;
            left: 0;
            top: 0;
            right: 0;
        }

        .editor {
            position: absolute;
            left: 0;
            top: 35px;
            right: 0;
            bottom: 0;
            border: 1px solid #ccc;
        }

        .props {
            position: absolute;
            right: 0;
            top: 35px;
            bottom: 0;
            width: 300px;
            border: 1px solid #ccc;
            border-top: none;
            overflow: auto;
        }

        .props-collapse {
            bottom: inherit !important;
            width: inherit !important;

            .box-title {
                display: none;
            }
        }
    }
</style>
