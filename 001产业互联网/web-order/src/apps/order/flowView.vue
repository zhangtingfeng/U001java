<template>
    <div id="widthmy" style="width: 100%;margin-left: 25px;">
        <svg :viewBox="`0 0 ${width} 50`" :width="`${width}px`" height="50px" version="1.1"
             xmlns="http://www.w3.org/2000/svg">
            <defs>
                <marker id="arrow" markerHeight="10" markerWidth="100" orient="auto" refX="0" refY="10"
                        viewBox="0 0 100 20">
                    <path d="M 0 0 L 0 0 L 0 0 z"/>
                </marker>
                <marker id="arrow-active" markerHeight="10" markerWidth="10" orient="auto" refX="0" refY="10"
                        viewBox="0 0 20 20">
                    <path d="M 0 0 L 0 0 L 0 0 z"/>
                </marker>
            </defs>
            <g :class="i <= active ? 'active' : ''" :key="d.id" :transform="`translate(${i * eachelewidth})`"
               v-for="(d, i) of datas">
                <rect rx="20" ry="20" height="20" width="20" y="10"/>
                <polyline points="5,20 9,23 15,16"
                          style="stroke-width:2"/>
                <text :x="10 - (d.text.length / 2.0 * 10)" y="47">
                    <tspan>{{d.text}}</tspan>
                </text>
                <line :marker-end="`url(#arrow${i <= active ? '-active' : ''})`" v-if="i < datas.length - 1" x1="20"
                      :x2="eachelelinewidth" y1="20" y2="20"/>
            </g>
        </svg>
    </div>
</template>

<script>
    import {dict} from "@f/framework";

    export default {
        props: ["name", "value"],
        data() {
            return {
                active: 1,
                datas: [],
                width: 100,
                eachelewidth: 200,
                eachelelinewidth: 0,
            };
        },
        watch: {
            name() {
                this.load();
            },
            value() {
                this.load();
            },
        },
        mounted() {

            // 激活Box
            setTimeout(() => {
                this.load();
            }, 400);


        },
        methods: {
            async load() {
                let name = this.name || "orderState1";
                this.datas = await dict.get(name);
                for (let i in this.datas) {
                    if (this.datas[i].id === this.value) {
                        this.active = parseInt(i);
                        break;
                    }
                }
                //  document.body.clientWidth
                var ele = document.getElementById('widthmy');
                let dddwidthmywidth = window.getComputedStyle(ele).width;
                let intdddwidthmywidth=parseInt(dddwidthmywidth)-50;
                this.eachelewidth = (intdddwidthmywidth-24) / ((this.datas.length-1)*1.0);
               // alert(this.eachelewidth);
               // alert(intdddwidthmywidth);
                // debugger;
                this.width = intdddwidthmywidth;// this.datas.length ? this.datas.length * 200 - 60 : 100;
                this.eachelelinewidth=this.eachelewidth;
                // alert(ddd);
            },
        },
    };
</script>

<style lang="less" scoped>

    rect {
        fill: #efefef;
    }
    polyline {

        fill:#efefef;
        stroke:#efefef;
    }


    text {
        font-size: 12px;
        fill: #333;
    }

    line {
        stroke: #ccc;
        stroke-width: 1px;
    }

    g.active {
        rect {
            fill: #6abf4b;
        }

        polyline {

            fill:#6abf4b;
            stroke:white;
        }


        text {
            fill: #fff;
        }

        line {
            stroke: #6abf4b;
        }
    }

    #arrow {
        fill: #ccc;
    }

    #arrow-active {
        fill: #6abf4b;
    }

    tspan {
        color: #333333 !important;
        fill: #333 !important;
    }
</style>
