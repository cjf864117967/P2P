function objToStr(obj) {
    var array = [];
    if (angular.isObject(obj)) {
        for (var key in obj) {
            array.push(key + '=' + obj[key]);
        }
        return array.join('&');
    } else {
        return obj;
    }
}

angular.module('AppController', [])

    .controller(
        'loginCtrl',
        function ($rootScope, $scope, $state, AdminService) { //将AdminService注入到了当前的loginCtrl中。

            $scope.errorMsg = '';

            $scope.login = function (username, password) {
                var str = 'username=' + username + '&password='
                    + password;
                AdminService.signIn(str).success(function (response) {
                    if (response.status == 1) {
                        $rootScope.LoginAdmin = username;
                        $state.go("home"); //改变状态
                    } else if (response.status == 0) {
                        $scope.errorMsg = '用户名或密码错误！';
                    } else {
                        $scope.errorMsg = '用户登录异常，请联系客服！';
                    }
                }).error(function () {
                    $scope.errorMsg = '网络异常，稍后重试！';
                    $scope.refresh();
                });
            };
            $scope.hideMsg = function () {
                $scope.errorMsg = '';
            };
        })

    // 空间首页
    .controller(
        'homeCtrl',
        function ($rootScope, $location, $scope, $state) {
            var arr = [];
            var len = 30;
            for (var i = 0; i < len; i++) {
                var strTem = '"a' + i + '":true';
                arr.push(strTem)
            }
            var str = "{" + arr.join(',') + '}';
            var obj = eval('(' + str + ')');
            $scope.rights = obj;
            if ($scope.rights.a1 || $scope.rights.a2
                || $scope.rights.a28) {
                $(".menu_list>li").eq(0).css("display", "block");
            }
            if ($scope.rights.a7 || $scope.rights.a4
                || $scope.rights.a5 || $scope.rights.a6
                || $scope.rights.a29) {
                $(".menu_list>li").eq(1).css("display", "block");
            }

            if ($scope.rights.a3) {
                $(".menu_list>li").eq(2).css("display", "block")
            }
            if ($scope.rights.a8 || $scope.rights.a9
                || $scope.rights.a10 || $scope.rights.a11
                || $scope.rights.a30 || $scope.rights.a31) {
                $(".menu_list>li").eq(3).css("display", "block")
            }
            if ($scope.rights.a13 || $scope.rights.a14
                || $scope.rights.a15 || $scope.rights.a16
                || $scope.rights.a17 || $scope.rights.a18
                || $scope.rights.a19 || $scope.rights.a20) {
                $(".menu_list>li").eq(4).css("display", "block")
            }
            if ($scope.rights.a21 || $scope.rights.a22) {
                $(".menu_list>li").eq(5).css("display", "block")
            }
            if ($scope.rights.a23) {
                $(".menu_list>li").eq(6).css("display", "block")
            }
            if ($scope.rights.a24 || $scope.rights.a25) {
                $(".menu_list>li").eq(7).css("display", "block")
            }
            if ($scope.rights.a26) {
                $(".menu_list>li").eq(8).css("display", "block")
            }
            if ($scope.rights.a27) {
                $(".menu_list>li").eq(9).css("display", "block")
            }
            $scope.path = $location.path();

            // 退出操作
            $scope.logout = function () {
                $rootScope.LoginAdmin = '';
                $state.go('login');
            }
        })

    // 产品列表
    .controller("productList", function ($scope, $state, ProductService) {

        //页面加载过程中，这个函数会执行
        $scope.getProductList = function () {

            ProductService.getProductList().success(function (response) {
                //{status:1,data:[{},{},{}]}
                if (response.status == 1) {
                    $scope.info = response.data;
                }
            });
        }
        // 调用函数
        $scope.getProductList();

        // 根据产品ID隐藏删除按钮的判断函数
        $scope.isHide = function (value) {
            // if (value != 1 || value != 2 || value != 3) {
            // return false;
            // } else {
            return true;
            // }
        }

        // 产品删除
        $scope.productDel = function (proId) {
            var str = 'proId=' + proId;
            ProductService.delProduct(str).success(function (response) {
                if (response.status == 1) {
                    $scope.getProductList();
                }
            })
        }

        // 根据ID获取产品信息
        $scope.viewProductById = function (proId) {
            var str = 'proId=' + proId
            ProductService.getProductById(str).success(function (response) {
                if (response.status == 1) {
                    $scope.view = response.data;
                    $scope.colseDown = true;
                    $scope.recordList = false;
                    $('#myModal_b').modal();
                }
            });
        };

        $scope.hide = function () {
            if (!$scope.recordList) {
                if ($scope.colseDown) {
                    $('#myModal_b').modal('hide');
                    $scope.recordList = true;
                    $scope.colseDown = false;
                } else {
                    $scope.recordList = true;
                }
            } else {
                $('#myModal_b').modal('hide');
            }
        }
    })
    // 产品添加
    .controller(
        "productAdd",
        function ($scope, $state, ProductService) {
            $scope.saveProduct = function () {
                var str = "";
                for (var x in $scope.productInfo) {
                    if (x === "proEarningRate") {
                        var rateStr = "";
                        for (var y = 0; y < $scope.productInfo.proEarningRate.length; y++) {
                            rateStr += '"'
                                + $scope.productInfo.proEarningRate[y].month
                                + '":'
                                + $scope.productInfo.proEarningRate[y].incomeRate
                                + ","
                        }
                        rateStr = '{'
                            + rateStr.substr(0, rateStr.length - 1)
                            + '}';
                        str += "&proEarningRates=" + rateStr;
                    } else {
                        str += "&" + x + "=" + $scope.productInfo[x];
                    }
                }
                if (str.substring(0, 1) === '&') {
                    str = str.substring(1);
                }
                alert(str);

                ProductService.saveProduct(str).success(
                    function (response) {
                        if (response.status == 1) {
                            $state.go("home.productlist");
                        }
                    })

            }

            $scope.cancelProduct = function () {
                $state.go("home.productlist");
            }
            // 设置利率
            $scope.addEarningRate = function () {
                var options = {
                    backdrop: "static"
                };
                $('#myModal_b').modal(options);

            }
            // 添加月利率设置
            $scope.addField = function () {

                if ($scope.productInfo.proEarningRate == null) {
                    $scope.productInfo.proEarningRate = [{
                        "month": "",
                        "incomeRate": ""
                    }];
                } else {
                    $scope.productInfo.proEarningRate.push({
                        "month": "",
                        "incomeRate": ""
                    });
                }

            }
        })
    // 修改产品
    .controller(
        "productEdit",
        function ($scope, $state, $stateParams, ProductService) {
            var proId = $stateParams.proId;
            var str = "proId=" + proId

            $scope.productInfo = "";
            // 调用函数,商品信息回显
            (function () {
                ProductService.getProductById(str).success(
                    function (response) {
                        if (response.status == 1) {
                            $scope.productInfo = response.data;
                        }
                    })
            })();

            // 真正提交修改
            $scope.commitProduct = function (proId) {
                var str = "";
                for (var x in $scope.productInfo) {
                    if (x === "proEarningRate") {
                        var rateStr = "";
                        for (var y = 0; y < $scope.productInfo.proEarningRate.length; y++) {
                            rateStr += '"'
                                + $scope.productInfo.proEarningRate[y].month
                                + '":'
                                + $scope.productInfo.proEarningRate[y].incomeRate
                                + ","
                        }
                        rateStr = '{'
                            + rateStr.substr(0, rateStr.length - 1)
                            + '}';
                        str += "&proEarningRates=" + rateStr;
                    } else {
                        str += "&" + x + "=" + $scope.productInfo[x];
                    }
                }
                if (str.substring(0, 1) === '&') {
                    str = str.substring(1);
                }
                alert(str);

                ProductService.commitProduct(str).success(
                    function (response) {
                        if (response.status == 1) {
                            $state.go("home.productlist");
                        }
                    });

            }

            $scope.cancelProduct = function () {
                $state.go("home.productlist");
            }

            // 设置利率
            $scope.change = function (proId) {
                /*var str = 'proId=' + proId;
                alert(str);*/
                var options = {
                    backdrop: "static"
                };
                $('#myModal_b').modal(options);
                // 获取当前利率
                /*ProductService.getRatesById(str).success(
                        function(response) {
                            if (response.status == 1) {
                                $scope.productInfo.proEarningRate = response.data;
                                var options = {
                                    backdrop : "static"
                                };
                                $('#myModal_b').modal(options);
                            }
                        });*/
            }
        })

    // 债权录入
    .controller(
        'entering',
        function ($scope, $state, AuthService, PostService, hmd,
                  checkParamService, $stateParams) {

            $scope.alertMsgs = []
            $scope.params = {
                // 默认值
                contractNo: undefined, // 借款Id（合同编号）
                debtorsName: undefined, // 债务人
                debtorsId: undefined, // 身份证号
                loanPurpose: undefined, // 借款用途
                loanType: undefined, // 借款类型（标的类型）
                loanPeriod: undefined, // 原始期限（月）源
                loanStartDate: undefined, // 原始借款开始日期
                loanEndDate: undefined, // 原始借款到期日期
                repaymentStyle: 11601, // 还款方式 radius
                repaymenMoney: undefined, // 期供金额（元）
                debtMoney: undefined, // 债权金额（元）
                debtMonthRate: undefined, // 债权年化利率（%// ）
                debtTransferredMoney: undefined, // 债权转入金额
                debtTransferredPeriod: undefined, // 债权可用期（月）
                debtTransferredDate: undefined, // 债权转入日期
                debtRansferOutDate: undefined, // 债权转出日期
                creditor: undefined
                // 债权人
            }

            function getDateVal() {
                $scope.params.loanStartDate = $("#dLoanStartDate")
                    .val()
                $scope.params.loanEndDate = $("#dLoanEndDate").val()
                $scope.params.debtTransferredDate = $(
                    "#dDebtTransferredDate").val()
                $scope.params.debtRansferOutDate = $(
                    "#dDebtRansferOutDate").val()
            }

            // 表单验证
            function formInvail() {
                var msg = []
                var debtorsId = $("#dDebtorsId").val(), loanPeriod = $(
                    "#dLoanPeriod").val(), debtMoney = $(
                    "#dDebtMoney").val(), repaymenMoney = $(
                    "#dRepaymenMoney").val(), debtMonthRate = $(
                    "#dDebtMonthRate").val(), debtTransferredMoney = $(
                    "#dDebtTransferredMoney").val(), debtTransferredPeriod = $(
                    "#dDebtTransferredPeriod").val();
                if (!checkParamService.AuthIDcard(debtorsId)) {
                    msg.push("身份证号码格式不正确！");
                }
                if (loanPeriod < 0 || !loanPeriod
                    || !loanPeriod.search(/\D/)) {
                    msg.push("原始期限必须为大于0的数字");
                }
                if (debtMoney < 0 || !debtMoney
                    || !debtMoney.search(/\D/)) {
                    msg.push("债权金额必须为大于0的数字");
                }
                if (repaymenMoney < 0 || !repaymenMoney
                    || !repaymenMoney.search(/\D/)) {
                    msg.push("期限金额必须为大于0的数字");
                }
                if (debtMonthRate < 0 || !debtMonthRate
                    || !debtMonthRate.search(/\D/)) {
                    msg.push("债权月利率必须为大于0的数字");
                }
                if (debtTransferredMoney < 0 || !debtTransferredMoney
                    || !debtTransferredMoney.search(/\D/)) {
                    msg.push("债权转入金额必须为大于0的数字");
                }
                if (debtTransferredPeriod < 0 || !debtTransferredPeriod
                    || !debtTransferredPeriod.search(/\D/)) {
                    msg.push("债权可用期限必须为大于0的数字");
                }
                if (!$("#dRepaymenDates").val()) {
                    msg.push("还款日不可为空");
                }

                if ($stateParams.dId) {
                    for (var i = 0; i < $scope.params.length - 2; i++) {
                        if (!$scope.params[i]) {
                            msg.unshift("所有债权录入数据不能为空！")
                            break;
                        }
                    }
                } else {
                    for (var i in $scope.params) {
                        if (!$scope.params[i]) {
                            msg.unshift("所有债权录入数据不能为空！")
                            break;
                        }
                    }
                }
                if (msg.length >= 1) {
                    hmd.popupErrorInfo(msg[0], "error")
                    return !!msg.length
                }
            }

            // 提交
            $scope.entering = function () {
                getDateVal();
                if (formInvail()) {
                    return;
                }

                var str = ""
                for (var i in $scope.params) {
                    str += "&" + i + "=" + $scope.params[i]

                }
                str = str.replace(/undefined/g, "") + "&repaymenDate="
                    + $("#dRepaymenDates").val();

                console.info("str============" + str);

                PostService.entryDebet(str).success(function (response) {
                    if (response.status == 1) {
                        hmd.popupErrorInfo("债权录入成功！", "ok");
                        $scope.params = '';
                    } else {
                        hmd.popupErrorInfo(response.status);
                    }
                });

            }

            $scope.reset = function () {
                for (var i in $scope.params) {
                    $scope.params[i] = ""
                }
                $scope.params.repaymentStyle = 11601
                $scope.params.repaymenDate = 7
            }
        })
    .controller(
        'multiple',
        function ($scope, AuthService, FileUploader, hmd) {

            var uploader = $scope.uploader = new FileUploader({
                url: '/itcast_p2p_action/creditor/upload'
            });

            uploader.filters
                .push({
                    name: 'customFilter',
                    fn: function (item /* {File|FileLikeObject} */,
                                  options) {
                        var type = '|'
                            + item.type.slice(item.type
                                .lastIndexOf('/') + 1)
                            + '|';
                        var condition = ('|vnd.ms-excel|vnd.openxmlformats-officedocument.spreadsheetml.sheet|x-excel|x-msdownload|'
                                .indexOf(type) !== -1)
                            && (this.queue.length < 10)
                        return true;
                    }
                });

            // 上传文件失败
            uploader.onWhenAddingFileFailed = function (
                item /* {File|FileLikeObject} */, filter, options) {
                hmd.popupErrorInfo('添加文件失败！', 'error');
            };

            uploader.onSuccessItem = function (fileItem, response,
                                               status, headers) {
                if (response.status == 1) {
                    fileItem.isSuccess = true;
                    fileItem.isError = false;
                } else {
                    if (response.status == 101) {
                        var str = '导入数据失败,第' + response.data.info
                            + '行，第' + response.data.errorCell
                            + '列数据有误';
                        hmd.popupErrorInfo(str, 'error');
                    } else {
                        hmd.popupErrorInfo(response.status);
                    }
                    fileItem.isSuccess = false;
                    fileItem.isError = true;
                }
            };
        })

    .controller(
        // 债权审核
        'rightcheckCtrl',
        function ($scope, $state, AuthService, PostService, hmd,
                  checkParamService) {
            // 分页
            $scope.totalItems = 0;
            $scope.maxSize = 10;
            $scope.currentPage = 1;
            $scope.itemsPerPage = 20;
            // 查询参数
            $scope.params = {}
            // 默认查询参数
            var params_default = {
                dDebtNo: null, // 标的编号
                dContractNo: null, // 借款ID
                dDebtTransferredDateStart: null, // 债权转入日期start
                dDebtTransferredDateEnd: null, // 债权转入日期end
                dDebtStatus: null, // 债权状态 全部: null 未审核: 11301 已审核:
                // 11302 正常还款: 11303 已结清: 11304
                // 提前结清: 11305
                dMatchedStatus: null
                // 债权匹配状态 全部: null 部分匹配: 11401 完全匹配: 11402 未匹配: 11403
                // offsetnum: 1, // 当前页码 default: 1
            }
            // 显示数据, 查询返回数据
            $scope.rights = [{
                dId: null, // 债权ID
                dDebtNo: null, // 债权编号（标的编号）
                dContractNo: null, // 借款Id（合同编号）
                dDebtorsName: null, // 债务人名称
                dDebtorsId: null, // 债务人身份证号
                dLoanPurpose: null, // 借款用途
                dLoanType: null, // 借款类型（标的类型
                dLoanPeriod: null, // 原始借款期限
                dLoanStartDate: null, // 原始借款开始日期
                dLoanEndDate: null, // 原始借款到期日期
                dRepaymentStyle: null, // 还款方式
                dRepaymenDate: null, // 还款日
                dRepaymenMoney: null, // 每期还款金额
                dDebtMoney: null, // 债权金额（元）
                dDebtYearRate: null, // 债权年化利率（%）
                dDebtTransferredMoney: null, // 债权转入金额
                dDebtTransferredDate: null, // 债权转入日期
                dDebtTransferredPeriod: null,// 债权转入期限
                dDebtRansferOutDate: null, // 债权转出日期
                dCreditor: null, // 债权人
                dDebtStatus: null, // 债权状态
                dDebtMonthRate: null, // 债权月利率（%
                dBorrowerId: null, // 借款人ID
                dAvailablePeriod: null, // 债权可用期数
                dAvailableMoney: null, // 债权可用金额
                dMatchedMoney: null, // 债权已匹配金额
                dMatchedStatus: null, // 债权匹配状态
                dDebtTypeName: null, // 标的类型名称
                dRepaymentStyleName: null, // 还款方式名称
                dDebtStatusName: null, // 债权状态名字
                dMatchedStatusName: null
                // 匹配状态名字
            }]
            // 合计数据
            $scope.datasume = {
                dIdCount: null, // 合计记录
                dDebtMoneySum: null, // 债权可用金额合计
                dAvailableMoneySum: null
                // 债权金额合计
            }
            // 弹出层数据
            $scope.panel = {
                dId: null, // 债权ID
                dDebtNo: null, // 债权编号(标的编号)
                dContractNo: null, // 借款Id(合同编号)
                dDebtorsName: null, // 债务人名称
                dDebtorsId: null, // 债务人身份证号
                dLoanPurpose: null, // 借款用途
                dLoanType: null, // 借款类型(标的类型)
                dLoanPeriod: null, // 原始借款期限

                dLoanStartDate: null, // 原始借款开始日期
                dLoanEndDate: null, // 原始借款到期日期

                dRepaymentStyle: 11601, // 还款方式
                dRepaymenDate: null, // 还款日
                dRepaymenMoney: null, // 每期还款金额
                dDebtMoney: null, // 债权金额(元)
                dDebtYearRate: null, // 债权年化利率(%)
                dDebtTransferredMoney: null, // 债权转入金额
                dDebtTransferredPeriod: null, // 债权转入期限

                dDebtTransferredDate: null, // 债权转入日期
                dDebtRansferOutDate: null, // 债权转出日期
                dCreditor: null, // 债权人
                dDebtStatus: null, // 债权状态
                dDebtMonthRate: null, // 债权年利率(%)
                dBorrowerId: null, // 借款人ID
                dAvailablePeriod: null, // 债权可用期数
                dAvailableMoney: null, // 债权可用金额
                dMatchedMoney: null, // 债权已匹配金额
                dMatchedStatus: null, // 债权匹配状态
                dRepaymentStyleName: null, // 还款方式
                dDebtStatusName: null, // 债权状态名字
                dMatchedStatusName: null
                // 匹配状态名字
            };

            // 重置
            $scope.reset = function () {
                for (var i in params_default) {
                    $scope.params[i] = params_default[i]
                }
                $("#dDebtTransferredDateStart").val("")
                $("#dDebtTransferredDateEnd").val("")
                $("#dDebtStatus").val("") // 债权状态
                $("#dMatchedStatus").val("") // 债权匹配状态
            };

            // 查询
            $scope.search = function (currentPage) {
                $scope.params.dDebtTransferredDateStart = $(
                    "#date_start").val()
                $scope.params.dDebtTransferredDateEnd = $("#date_end")
                    .val()
                $scope.params.dDebtStatus = $("#dDebtStatus").val() // 债权状态
                $scope.params.dMatchedStatus = $("#dMatchedStatus")
                    .val() // 债权匹配状态
                var str = ""
                for (var i in $scope.params) {
                    str += "&" + i + "=" + $scope.params[i]
                }
                str = str.replace(/null/g, "")
                    .replace(/undefined/g, "")
                str = str.substring(1) + "&offsetnum=" + currentPage
                alert(str);
                PostService.checkCredit(str).success(
                    function (response) {
                        if (response.status == 1) {
                            var rights = response.data.date;// 列表数据

                            var datasum = response.data.datasum[0];// 合计数据

                            $scope.rights = rights;
                            $scope.datasume = datasum;
                            $scope.totalItems = datasum.dIdCount;
                        } else {
                            hmd.popupErrorInfo(response.status);
                        }
                    })
            };
            // 提前结清
            $scope.selectStr = '';
            $scope.comfirm = '';
            $scope.settlement = function (_status) {
                var ids = [], status = [], obj = '';
                $("tbody input[type='checkbox']").each(function () {
                    if ($(this).is(':checked')) {
                        var value = $(this).val()
                        var arr = value.split(",")
                        var id = arr[0]
                        var statu = arr[1]
                        ids.push(id);
                        status.push(statu);
                        obj = $(this).attr('_data');
                    }
                });
                if (!ids.length) {
                    hmd.popupErrorInfo('请选择需要提前结清的债权。', 'attention');
                    return false;
                }

                if (ids.length > 1) {
                    hmd
                        .popupErrorInfo('提前结清的标的只可以一次选择一个！',
                            'attention');
                    return false;
                }
                for (var i = 0; i < status.length; i++) {
                    if (status[i] != 11303) {
                        hmd.popupErrorInfo('只有正常还款的债权能够提前结清。',
                            'attention');
                        return false;
                    }
                }

                if (_status == 1) {
                    $('#confirmBox').modal('show');
                    $scope.comfirm = eval('(' + obj + ')');
                    $scope.selectStr = ids.toString();
                } else if (_status == 2) {
                    $('#newconfirmBox').modal('show');
                    $scope.newcomfirm = eval('(' + obj + ')');
                    $scope.newselectStr = ids.toString();
                }

            }

            $scope.submitConfirm = function () {
                var str = "token=" + token + "&status=11303"
                    + "&claimid=" + $scope.selectStr
                PostService.settlement(str).success(function (response) {

                    hmd.popupErrorInfo(response.status);
                    $('#confirmBox').modal('hide');
                })
            };

            $scope.newsubmitConfirm = function () {
                var str = "token=" + token + "&status=11303"
                    + "&claimid=" + $scope.newselectStr
                PostService.advanceSettle(str).success(
                    function (response) {

                        hmd.popupErrorInfo(response.status);
                        $('#newconfirmBox').modal('hide');
                    })
            };
            // 审核
            $scope.auditObj = {
                ids: [],
                status: [],
                name: undefined,
                confirm: function () {
                    this.ids = [];
                    this.status = [];
                    var status = this.status;
                    var ids = this.ids;
                    $("tbody input[type='checkbox']").each(function () {
                        if ($(this).is(':checked')) {
                            var value = $(this).val()
                            var id = value.split(",")[0];
                            var statu = value.split(",")[1];
                            status.push(statu);
                            ids.push(id);
                        }
                    });
                },
                // 债权提交
                submit: function () {
                    var status = this.status;
                    var length = status.length
                    for (var i = 0; i < status.length; i++) {
                        if (status[i] != 11301) {
                            hmd.popupErrorInfo(
                                '只有状态为\“未审核\”的债权能够进入\“待匹配债权队列\”',
                                'error')
                            return false;
                        }
                    }
                    var ids = this.ids.toString();
                    var audit_result = "", // 审核不通过理由，默认值:""
                        audit_flg = "1"; // 审核状态（审核不通过为0，审核为1）, 默认值:1
                    var str = "ids=" + ids
                        + "&audit_result=" + audit_result
                        + "&audit_flg=" + audit_flg
                    PostService.verifyCredit(str).success(
                        function (response) {
                            if (response.status == 1) {
                                var msg = "审核人：admin"
                                    + "<br />记录：" + length
                                    + "条<br />" + "审核结果已生效"
                                hmd.popupErrorInfo(msg, 'ok');
                                $scope.search($scope.currentPage)
                            } else if (response.status == 61) {
                                hmd.popupErrorInfo('请选择需要审核的债权。',
                                    'error');
                            } else {
                                hmd.popupErrorInfo('审核债权，传入参数有误。。',
                                    'error');
                            }
                        });
                }
            }
            // 发送查看详情请求
            var select = function (id) {
                var str = "token=" + token + "&dDebtNo=" + id;
                PostService.viewCreditDetail(str).success(
                    function (response) {
                        if (response.status == 1) {
                            $scope.panel = response.data[0]
                        } else {
                            hmd.popupErrorInfo(response.status);
                        }
                    });
            }
            // 是否显示 "编辑"/"删除" 按钮: only “债权状态 dDebtStatus” == “未审核 11301”
            // return true
            $scope.editShow = function (msg) {
                return msg == "11301" ? true : false
            };
            // 查看债权详情
            $scope.detail = function (id) {
                $scope.editable = false;
                select(id)
            };
            // 编辑
            $scope.eidtor = function (id) {
                $scope.editable = true;
            };
            $scope.save = function () {
                var str = "token=" + token
                for (var i in $scope.panel) {
                    str += "&" + i + "=" + $scope.panel[i]
                }
                str = str.replace(/null/g, "")
                    .replace(/undefined/g, "")
                PostService.modificationCredit(str).success(
                    function (response) {
                        if (response.status == 1) {
                            hmd.popupErrorInfo('债权修改成功', 'ok');
                            $scope.search($scope.currentPage)
                        }
                    });
            }
            // 删除
            $scope.delet = function (id) {
                var str = "token=" + token + "&dDebtNo=" + id;
                PostService.deleteCredit(str).success(
                    function (response) {
                        if (response.status == 1) {
                            hmd.popupErrorInfo('删除成功！', 'ok');
                            $scope.search($scope.currentPage)
                        } else {
                            hmd.popupErrorInfo(response.status);
                        }
                    });
            }
            // 导出
            $scope.exportCredit = function () {
                var str = ""
                for (var i in $scope.params) {
                    if (i == "offsetnum")
                        continue;
                    str += "&" + i + "=" + $scope.params[i];
                }
                str = str.replace(/null/g, '').replace(/undefined/, '');
                str = "token=" + token + "&" + str.substring(1);
                window.location.href = "/api/api/creditor/exportCreditor.do?"
                    + str;
            };
            var onload = function () {
                $scope.reset()
                $scope.search($scope.currentPage)
            };
            onload();
        })

    // 撮合操作
    // 待匹配资金队列
    .controller(
        'matchfundqueueCtrl',
        function ($scope, $state, AuthService, PostService, hmd) {
            var str = '';

            $scope.totalItems = 0;
            $scope.maxSize = 10;
            $scope.currentPage = 1;
            $scope.itemsPerPage = 20;
            var manage_count = $('#manage_count');
            var manage_sum = $('#manage_sum');

            // 查询待匹配资金队列条件组装
            function getDataByParams(pageNo) {
                $scope.currentPage = pageNo;
                var username = $('#username').val();
                var button_query = $('#button_query');
                var pSerialNo = $('#investserial').val();
                var endDate = $('#endDate').val();
                var productName = $('#productName').find(
                    'option:selected').attr('value');
                var investType = $('#investType').find(
                    'option:selected').attr('value');
                var str = 'userName=' + username + '&pSerialNo='
                    + pSerialNo + '&endDate=' + endDate
                    + '&productName=' + productName
                    + '&investType=' + investType;

                checkDataByPostService(str, pageNo);

            }
            ;

            // 查询待匹配资金队列
            function checkDataByPostService(param, page) {
                PostService
                    .selectWaitMoney(param + "&page=" + page)
                    .success(
                        function (response) {
                            if (response.status == 1) {
                                var data = response.data;
                                $scope.userList = data.listMatch;
                                $scope.totalItems = manage_count
                                    .html().replace(/\s/g,
                                        '');
                                manage_count
                                    .html(data.waitMatchCount.count);
                                manage_sum
                                    .html((+data.waitMatchCount.sum)
                                        .toFixed(2));
                            } else {
                                hmd
                                    .popupErrorInfo(response.status);
                            }
                        });

            }
            ;

            function init_query() {
                checkDataByPostService("", 1);
            }

            init_query();
            $scope.queryData = getDataByParams(1);
            // 开始匹配
            $scope.startmatching = function () {
                PostService.startMatchByManually().success(
                    function (response) {
                        hmd.popupErrorInfo(response.status);
                    }).error(function (response) {
                    hmd.popupErrorInfo(response.status);
                });

            };

        })

