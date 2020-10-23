<template>
    <div :onclick="curReadOnly?'return false':'return true'" class="boxbody">

        <div class="row boxbodycheckbox">
            <label class="col-md-3">原果规格</label>
        </div>
        <div class="row boxbodycheckbox" style="margin-left: 0px;">
            <span v-for="(contractOBJ,index) in contractSpecificationList[0].nameList">
                          <input type="checkbox"
                                 v-model="specificationsList[contractSpecificationList[0].Posstart+index]">{{contractOBJ}}
                    </span>
        </div>

        <div class="row boxbodycheckbox">
            <label class="col-md-3">成品规格</label>
        </div>
        <div class="row boxbodycheckbox" style="margin-left: 0px;">
          <span v-for="(contractOBJ,index) in contractSpecificationList[1].nameList">
                          <input type="checkbox"
                                 v-model="specificationsList[contractSpecificationList[1].Posstart+index]">{{contractOBJ}}
                    </span>
        </div>

        <div class="row boxbodycheckbox" style="margin-left: 0px;" v-for="(bigindex) in [2,3,4,5]">
             <span v-for="(contractOBJ,index) in contractSpecificationList[bigindex].nameList">
                          <input type="checkbox"
                                 v-model="specificationsList[contractSpecificationList[bigindex].Posstart+index]">{{contractOBJ}}
                    </span>
        </div>

        <div class="row boxbodycheckbox">
            <label class="col-md-3">质量</label>
        </div>
        <div class="row boxbodycheckbox" style="margin-left: 0px;">
            <span v-for="(contractOBJ,index) in contractSpecificationList[6].nameList">
                          <input type="checkbox"
                                 v-model="specificationsList[contractSpecificationList[6].Posstart+index]">{{contractOBJ}}
                    </span>

        </div>
    </div>
</template>

<script>
    export default {
        name: "ContractSpecification",
        props: {
            // v-model默认传入属性值
            curReadOnly: Boolean,
            specificationsList: Array
        },
        data() {
            return {

                contractSpecificationList: require("../../../../static/contractSpecification.json"),

            }
        },
        methods: {},


    }
</script>

<style lang="less" scoped>
    @import url("@/common/common.less");
</style>
