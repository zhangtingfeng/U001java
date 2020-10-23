<template>
    <div>
        <ul class="star">
            <li :class="item" @click="clickstart(item,index)" v-for="(item,index) in chanceList"></li>
            <!--根据数据循环创建li标签-->
        </ul>
    </div>
</template>

<script>
    export default {
        name: "star",
        data() {
            return {
                chanceList: [],
                numAllInteger:0,
                storInterger:0
                // num:5,
                // stor:3.5,
                // star: ["on", "on", "on", "off", "off"]
            }
        },
        props: ["numAllarg", "storScorearg","curReadOnly"], // 接受参数
        mounted() {
            //debugger;
            this.numAllInteger=this.numAllarg;
            this.storInterger=this.storScorearg;

            this.resetchance();

        },

        methods: {
            resetchance() {
                // 创建一个数组，让数组里的每个值都是off，好进行下面的比较。
                let arr = [];
                while (this.numAllInteger > arr.length) {
                    arr.push("off")
                }
              //  debugger;
                //向下取整数,数组中的Array.map() 方法返回一个新数组，此方法可以以一个函数为参数，循环数组的每一个元素，函数将数组中的元素接收为单个参数
                //判断整数大于下标值，大于的话返回on,因为数组的下标从0开始
                // 多出来的小数(res)乘10,大于等于5，使定义的新数组的num变成half，因为数组的下标从0开始。
                let num = Math.floor(this.storInterger)
                let res = (this.storInterger - num) * 10
                let newstar = arr.map((item, index) => {
                    if (num > index) {
                        return "on"
                    } else {
                        return "off"
                    }
                })
                if (res >= 5) {
                    newstar[num] = "half"
                }
               // debugger;
                this.chanceList = newstar //返回newstar数组

            },
            clickstart(argTypeInfo, argindex) {
               // debugger;
                if (this.curReadOnly) return;
                let destMubiao = "";
                let numScore=0;
                if (argTypeInfo == "on") {
                    destMubiao = "off";
                    numScore=0;
                } else if (argTypeInfo == "off") {
                    destMubiao = "half";
                    numScore=0.5;
                } else if (argTypeInfo == "half") {
                    destMubiao = "on";
                    numScore=1;
                }

                let allScore=numScore+argindex;
                this.storInterger=allScore;
                this.resetchance();


                this.$emit('transfer',this.storInterger)//将值绑定到transfer上传递过去
                //this.chanceList[argindex]=
            }

        }
    }
</script>

<style scoped>
    .star{
        /*border:1px solid red;*/
        display: inline-block;
        margin:0; padding:0;
    }
    .star li {

margin-right: 20px;
        width: 20px;
        height: 20px;
        float: left;
        list-style: none;
        background: url("./img/grey.png") no-repeat center/cover;
    }

    .star .on {
        background-image: url("./img/full.png");
    }

    .star .half {
        background-image: url("./img/half.png");
    }
</style>

