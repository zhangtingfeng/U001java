import {ajax} from "@f/vendor";

//let ajax = require("@f/vendor");


let listpubFuncton = {
    async getDescFunction(argORgCode) {
        let let_List = await this.mypubFuncton();


        for (let diii = 0; diii < let_List.length; diii++) {
            //  debugger;
            if (let_List[diii].id == argORgCode) {
                return let_List[diii].rltAcc;
                break;
            }
        }

        /*
        for(let dItemdd in let_List){
            debugger;
            if (dItemdd.value==argORgCode){
                return dItemdd.name;
                break;
            }
        }
 */
        return argORgCode;
    },

    async pubFuncton() {
        let let_List = await this.mypubFuncton();
        // debugger;
        return let_List;
        // return [];
    },

    async mypubFuncton() {

        let banklist = [];
        let letCompnayid = "";
        await ajax.post({
            url: 'api/trade/meta/company/1?pageSize=20',
            data: {}
        }).then(data => {
            letCompnayid = data.company.data[0].id;
            //debugger;


        });

        await ajax.post({
            url: 'api/trade/meta/account/query',
            data: {"companyId": letCompnayid}
        }).then(dataaccountList => {
            //                        let letCompnayid= data.company.data[0].id;
            //                      debugger;
            //debugger;
            banklist = dataaccountList.account;
            // this.classes = data;
            // this.goodsclass = parseTree(data);
        });

        //debugger;
        return banklist;
    },
    getShowTextaboutDicValue(dictValue, dictList) {
      // debugger;
        for(let i=0;i<dictList.length;i++){
            if (dictList[i].id==dictValue){
                return dictList[i].text;
            }
        }
        // return [];
    },
    async getShowTextaboutDicName(dictValue, dictName) {
        // debugger;
        let dictList = await dict.get(dictName);
        return this.getShowTextaboutDicValue(dictValue,dictList);

        // return [];
    },
    convertCurrency(currencyDigits) {
        let MAXIMUM_NUMBER = 99999999999.99;
        let CN_ZERO = "零";
        let CN_ONE = "壹";
        let CN_TWO = "贰";
        let CN_THREE = "叁";
        let CN_FOUR = "肆";
        let CN_FIVE = "伍";
        let CN_SIX = "陆";
        let CN_SEVEN = "柒";
        let CN_EIGHT = "捌";
        let CN_NINE = "玖";
        let CN_TEN = "拾";
        let CN_HUNDRED = "佰";
        let CN_THOUSAND = "仟";
        let CN_TEN_THOUSAND = "万";
        let CN_HUNDRED_MILLION = "亿";
        let CN_SYMBOL = "";
        let CN_DOLLAR = "元";
        let CN_TEN_CENT = "角";
        let CN_CENT = "分";
        let CN_INTEGER = "整";


        let integral;
        let decimal;
        let outputCharacters;
        let parts;
        let digits, radices, bigRadices, decimals;
        let zeroCount;
        let i, p, d;
        let quotient, modulus;


        currencyDigits = currencyDigits.toString();
        if (currencyDigits == "") {
            return "";
        } else {
            let strtemp = currencyDigits.split("-");
            if (strtemp.length > 1) {
                currencyDigits = strtemp[1]
            }
        }
        if (currencyDigits.match(/[^,.\d]/) != null) {
            return "";
        }
        if ((currencyDigits).match(/^((\d{1,3}(,\d{3})*(.((\d{3},)*\d{1,3}))?)|(\d+(.\d+)?))$/) == null) {
            return "";
        }


        currencyDigits = currencyDigits.replace(/,/g, "");
        currencyDigits = currencyDigits.replace(/^0+/, "");

        if (Number(currencyDigits) > MAXIMUM_NUMBER) {
            return "";
        }
        parts = currencyDigits.split(".");
        if (parts.length > 1) {
            integral = parts[0];
            decimal = parts[1];
            decimal = decimal.substr(0, 2);
        } else {
            integral = parts[0];
            decimal = "";
        }
        digits = new Array(CN_ZERO, CN_ONE, CN_TWO, CN_THREE, CN_FOUR, CN_FIVE, CN_SIX, CN_SEVEN, CN_EIGHT, CN_NINE);
        radices = new Array("", CN_TEN, CN_HUNDRED, CN_THOUSAND);
        bigRadices = new Array("", CN_TEN_THOUSAND, CN_HUNDRED_MILLION);
        decimals = new Array(CN_TEN_CENT, CN_CENT);
        outputCharacters = "";
        if (Number(integral) > 0) {
            zeroCount = 0;
            for (i = 0; i < integral.length; i++) {
                p = integral.length - i - 1;
                d = integral.substr(i, 1);
                quotient = p / 4;
                modulus = p % 4;
                if (d == "0") {
                    zeroCount++;
                } else {
                    if (zeroCount > 0) {
                        outputCharacters += digits[0];
                    }
                    zeroCount = 0;
                    outputCharacters += digits[Number(d)] + radices[modulus];
                }
                if (modulus == 0 && zeroCount < 4) {
                    outputCharacters += bigRadices[quotient];
                }
            }
            outputCharacters += CN_DOLLAR;
        }

        if (decimal != "") {
            for (i = 0; i < decimal.length; i++) {
                d = decimal.substr(i, 1);
                if (d != "0") {
                    outputCharacters += digits[Number(d)] + decimals[i];
                }
            }
        }
        if (outputCharacters == "") {
            outputCharacters = CN_ZERO + CN_DOLLAR;
        }
        if (decimal == "" || decimal == 0) {
            outputCharacters += CN_INTEGER;
        }
        outputCharacters = CN_SYMBOL + outputCharacters;
        return outputCharacters;
    }


}


/*
async function listpubFuncton() {
    let banklist = [];
    let letCompnayid = "";
    await ajax.post({
        url: 'api/trade/meta/company/1?pageSize=20',
        data: {}
    }).then(data => {
        letCompnayid = data.company.data[0].id;
        //debugger;


    });
    await ajax.post({
        url: 'api/trade/meta/account/query',
        data: {"companyId": letCompnayid}
    }).then(dataaccountList => {
        //                        let letCompnayid= data.company.data[0].id;
        //                      debugger;
        //debugger;
        banklist = dataaccountList.account;
        // this.classes = data;
        // this.goodsclass = parseTree(data);
    });


    return banklist;
}*/
/*
module.exports={

    pubFuncton,
}*/

export default listpubFuncton;
