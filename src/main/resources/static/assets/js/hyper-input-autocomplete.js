"use strict";
var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (Object.prototype.hasOwnProperty.call(b, p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        if (typeof b !== "function" && b !== null)
            throw new TypeError("Class extends value " + String(b) + " is not a constructor or null");
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var AutoComplete = /** @class */ (function () {
    function AutoComplete(input, list) {
        this.currentFocus = -1;
        this.input = input;
        this.list = list;
        this.container = (input.parentNode.parentNode);
        this.handler();
    }
    AutoComplete.prototype.handler = function () {
        var _this = this;
        this.input.addEventListener("input", function (e) {
            var a, b, val = _this.input.value;
            _this.closeAllLists();
            if (!val) {
                return false;
            }
            _this.currentFocus = -1;
            a = document.createElement("DIV");
            a.setAttribute("id", _this.input.id + "autocomplete-list");
            a.setAttribute("class", "autocomplete-items");
            _this.container.appendChild(a);
            var fragment_refresh = {
                url: '',
                type: 'POST',
                data: {
                    time: new Date().getTime()
                },
                timeout: 5000,
                beforeSend: function () {
                },
                success: function (data) {
                },
                error: function () {
                }
            };
            jQuery.ajax(fragment_refresh);
            // jQuery.ajax(fragment_refresh).complete(() => { });
            _this.list.forEach(function (Element) {
                if (Element.substr(0, val.length).toUpperCase() == val.toUpperCase()) {
                    b = document.createElement("DIV");
                    b.innerHTML = "<strong>" + Element.substr(0, val.length) + "</strong>";
                    b.innerHTML += Element.substr(val.length);
                    b.innerHTML += "<input type='hidden' value='" + Element + "'>";
                    b.addEventListener("click", _this.attachment.bind(_this, b));
                    a.appendChild(b);
                }
            });
        });
        this.input.addEventListener("keydown", function (e) {
            var x = document.getElementById(_this.input.id + "autocomplete-list");
            if (x) {
                x = (x.getElementsByTagName("div"));
            }
            if (e.keyCode == 40) {
                _this.currentFocus++;
                _this.addActive(x);
            }
            else if (e.keyCode == 38) {
                _this.currentFocus--;
                _this.addActive(x);
            }
            else if (e.keyCode == 13) {
                e.preventDefault();
                if (_this.currentFocus > -1) {
                    if (x) {
                        x[_this.currentFocus].click();
                    }
                }
            }
        });
        document.addEventListener("click", function (e) {
            _this.closeAllLists((e.target));
        });
    };
    AutoComplete.prototype.attachment = function (target) {
        this.input.value = target.querySelector("input").value;
        this.closeAllLists();
    };
    AutoComplete.prototype.addActive = function (x) {
        if (!x)
            return false;
        this.removeActive(x);
        if (this.currentFocus >= x.length)
            this.currentFocus = 0;
        if (this.currentFocus < 0)
            this.currentFocus = (x.length - 1);
        x[this.currentFocus].classList.add("autocomplete-active");
    };
    AutoComplete.prototype.removeActive = function (x) {
        for (var i = 0; i < x.length; i++) {
            x[i].classList.remove("autocomplete-active");
        }
    };
    AutoComplete.prototype.closeAllLists = function (target) {
        var _this = this;
        if (target === void 0) { target = true; }
        this.container.querySelectorAll(".autocomplete-items").forEach(function (Element) {
            if (target && target != Element && target != _this.input) {
                Element.parentNode.removeChild(Element);
            }
        });
    };
    return AutoComplete;
}());
var Chips = /** @class */ (function (_super) {
    __extends(Chips, _super);
    function Chips(chipsContainer) {
        var _this = _super.call(this, document.querySelector("input.has-cat-autocomplete"), ["ایران", "آموزش", "فرهنگ", "فلسفه", "ادبیات", "هنر", "سیاسی", "منطق", "اقتصادی", "سرمایه داری و آسیب ها", "مفتی بی ریشه و ریشه ی مفتی", "سبیل نیچه"]) || this;
        _this.chipsContainer = chipsContainer;
        return _this;
    }
    Chips.prototype.attachment = function (target) {
        this.input.value = target.querySelector("input").value;
        var span = document.createElement("SPAN");
        span.className = "remove";
        span.innerHTML = "<i class='icon-close'></i>";
        var li = document.createElement("LI");
        li.setAttribute("title", this.input.value);
        li.appendChild(span);
        li.appendChild(document.createTextNode(this.input.value));
        new ChipsItem(span, li);
        this.chipsContainer.appendChild(li);
        this.closeAllLists();
    };
    return Chips;
}(AutoComplete));
var ChipsItem = /** @class */ (function () {
    function ChipsItem(span, item) {
        span.addEventListener("click", function (e) {
            item.remove();
        });
    }
    return ChipsItem;
}());
new Chips(document.querySelector(".cat-addition-result ul"));
