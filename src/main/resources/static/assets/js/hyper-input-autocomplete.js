"use strict";
var AutoComplete = /** @class */ (function () {
    function AutoComplete(input, name) {
        this.currentFocus = -1;
        this.input = input;
        this.inputName = name;
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
            _this.sendRequest(a, val);
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
        if (target)
            this.input.value = target.querySelector("input").value;
        if (this.input.value.trim() != '' && !AutoComplete.inputValueArray.includes(this.input.value.trim())) {
            var div = document.createElement("DIV");
            var span = document.createElement("SPAN");
            var inp = document.createElement("INPUT");
            div.setAttribute("title", this.input.value.trim());
            span.className = "remove";
            span.innerHTML = "<i class='icon-close'></i>";
            inp.setAttribute("name", "category");
            inp.setAttribute("value", this.input.value.trim());
            div.appendChild(span);
            div.appendChild(inp);
            new ChipsItem(span, div, this.input.value.trim());
            this.container.appendChild(div);
            AutoComplete.inputValueArray.push(this.input.value.trim());
            this.input.value = "";
        }
    };
    AutoComplete.prototype.sendRequest = function (a, val) {
        var _this = this;
        var b;
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
                var div = document.createElement("DIV");
                var button = document.createElement("BUTTON");
                div.className = "search-progress";
                div.innerHTML = "<span>موردی یافت نشد !</span>";
                button.setAttribute("type", "button");
                button.className = "btn btn-primary";
                button.innerHTML = "افزودن جدید";
                button.addEventListener("click", _this.attachment.bind(_this, null));
                div.appendChild(button);
                a.appendChild(div);
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
    AutoComplete.inputValueArray = [];
    return AutoComplete;
}());
var ChipsItem = /** @class */ (function () {
    function ChipsItem(span, div, value) {
        span.addEventListener("click", function (e) {
            div.remove();
            var index = AutoComplete.inputValueArray.indexOf(value);
            if (index > -1) {
                AutoComplete.inputValueArray.splice(index, 1);
            }
        });
    }
    return ChipsItem;
}());
new AutoComplete(document.getElementById("cat-add"), "category");
