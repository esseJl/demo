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
    function AutoComplete(input) {
        this.currentFocus = -1;
        this.input = input;
        this.container = (input.parentNode.parentNode);
        this.handler();
    }
    AutoComplete.prototype.handler = function () {
        var _this = this;
        this.input.addEventListener("input", function (e) {
            var a, requestTimeout, val = _this.input.value.trim();
            _this.closeAllLists();
            if (!val) {
                return false;
            }
            _this.currentFocus = -1;
            a = document.createElement("DIV");
            a.setAttribute("id", _this.input.id + "autocomplete-list");
            a.setAttribute("class", "autocomplete-items");
            _this.container.appendChild(a);
            a.innerHTML = '<div class="search-progress"><span>در حال جستجو ..</span><div class="loader"></div></div>';
            clearTimeout(requestTimeout);
            requestTimeout = setTimeout(_this.sendRequest.bind(_this, a, val), 1500);
        });
        this.input.addEventListener("keydown", function (e) {
            var x = document.querySelectorAll("#" + _this.input.id + "autocomplete-list > div:not(.search-progress):not(.search-progress)");
            if (!x) {
                return false;
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
    AutoComplete.prototype.sendRequest = function (a, val) {
        var _this = this;
        var b;
        a.innerHTML = '';
        jQuery.getJSON(window.location.origin + '/admin/post/category/' + this.input.value.trim(), function (data) {
            a.innerHTML = '';
            data.forEach(function (Element) {
                if (Element.substr(0, val.length).toUpperCase() == val.toUpperCase()) {
                    b = document.createElement("DIV");
                    b.innerHTML = "<strong>" + Element.substr(0, val.length) + "</strong>";
                    b.innerHTML += Element.substr(val.length);
                    b.innerHTML += "<input type='hidden' value='" + Element + "'>";
                    b.addEventListener("click", _this.attachment.bind(_this, b));
                    a.appendChild(b);
                }
            });
            if (!data.length) {
                a.innerHTML = '<div class="search-progress"><span>موردی یافت نشد !</span></div>';
            }
        }).fail(function () {
            console.log("error in category ajax");
        });
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
                Element.remove();
            }
        });
    };
    return AutoComplete;
}());
var Chips = /** @class */ (function (_super) {
    __extends(Chips, _super);
    function Chips(chipsContainer) {
        var _this = _super.call(this, document.querySelector("input.has-cat-autocomplete")) || this;
        _this.addNewBtn = _this.input.nextElementSibling;
        _this.chipsContainer = chipsContainer;
        Chips.inputTemp = chipsContainer.previousElementSibling;
        _this.addNewBtn.addEventListener("click", function (e) {
            if (_this.input.value.trim() != '' && !Chips.inputValueArray.includes(_this.input.value.trim())) {
                var span = document.createElement("SPAN");
                span.className = "remove";
                span.innerHTML = "<i class='icon-close'></i>";
                var li = document.createElement("LI");
                li.setAttribute("title", _this.input.value);
                li.appendChild(span);
                li.appendChild(document.createTextNode(_this.input.value));
                new ChipsItem(span, li, _this.input.value);
                _this.chipsContainer.appendChild(li);
                Chips.inputValueArray.push(_this.input.value.trim());
                Chips.inputTemp.value = Chips.inputValueArray.toString();
                _this.input.value = "";
            }
        });
        return _this;
    }
    Chips.prototype.attachment = function (target) {
        this.input.value = target.querySelector("input").value;
        if (!Chips.inputValueArray.includes(this.input.value)) {
            var span = document.createElement("SPAN");
            span.className = "remove";
            span.innerHTML = "<i class='icon-close'></i>";
            var li = document.createElement("LI");
            li.setAttribute("title", this.input.value);
            li.appendChild(span);
            li.appendChild(document.createTextNode(this.input.value));
            new ChipsItem(span, li, this.input.value);
            this.chipsContainer.appendChild(li);
            Chips.inputValueArray.push(this.input.value);
            Chips.inputTemp.value = Chips.inputValueArray.toString();
            this.input.value = "";
        }
        this.closeAllLists();
    };
    Chips.inputValueArray = [];
    return Chips;
}(AutoComplete));
var ChipsItem = /** @class */ (function () {
    function ChipsItem(span, item, value) {
        span.addEventListener("click", function (e) {
            item.remove();
            var index = Chips.inputValueArray.indexOf(value);
            if (index > -1) {
                Chips.inputValueArray.splice(index, 1);
            }
            Chips.inputTemp.value = Chips.inputValueArray.toString();
        });
    }
    return ChipsItem;
}());
new Chips(document.querySelector(".cat-addition-result ul"));
