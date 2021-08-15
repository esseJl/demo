"use strict";
var LayoutFunctions = /** @class */ (function () {
    function LayoutFunctions() {
        this.isDisabled = false;
        this.searchSugestion = document.querySelector('.search-form .dropdown-menu');
        this.sidenavCollapse = document.querySelectorAll('.side-nav .accordion-collapse');
        this.sidenavCollapseBtn = document.querySelectorAll('.side-nav .accordion-button');
        this.checkStorage();
        this.init();
    }
    LayoutFunctions.prototype.checkStorage = function () {
        if (typeof (Storage) !== "undefined" && localStorage.getItem("hyperSide")) {
            document.body.classList.add('hide-side-nav');
            this.accordionState(true);
        }
    };
    LayoutFunctions.prototype.accordionState = function (state) {
        if (state) {
            this.sidenavCollapse.forEach(function (Element) {
                Element.classList.add('show');
            });
            this.sidenavCollapseBtn.forEach(function (Element) {
                Element.setAttribute('aria-expanded', 'true');
            });
            this.isDisabled = true;
        }
        else {
            this.sidenavCollapse.forEach(function (Element) {
                Element.classList.remove('show');
            });
            this.sidenavCollapseBtn.forEach(function (Element) {
                Element.setAttribute('aria-expanded', 'false');
                Element.classList.add('collapsed');
            });
            this.isDisabled = false;
        }
    };
    LayoutFunctions.prototype.init = function () {
        var _this = this;
        document.querySelector('.setting-btn-item .btn').addEventListener("click", function () {
            document.body.classList.toggle('show-setting-bar');
        });
        document.querySelector('.hamburger-btn').addEventListener("click", function () {
            document.body.classList.toggle('hide-side-nav');
            if (document.body.classList.contains('hide-side-nav')) {
                localStorage.setItem("hyperSide", "active");
                _this.accordionState(true);
            }
            else {
                localStorage.removeItem("hyperSide");
                _this.accordionState(false);
            }
        });
        document.querySelector('.search-form input').addEventListener("focus", function () {
            _this.searchSugestion.classList.add('show');
        });
        document.querySelector('.setting-bar .bar-title a').addEventListener("click", function () {
            document.body.classList.remove('show-setting-bar');
        });
        document.querySelector('.search-form input').addEventListener("blur", function () {
            setTimeout(function () {
                _this.searchSugestion.classList.remove('show');
            }, 100);
        });
        document.querySelectorAll('.side-nav .accordion').forEach(function (Element) {
            Element.addEventListener('hide.bs.collapse', function (e) {
                if (_this.isDisabled) {
                    e.preventDefault();
                }
            });
        });
    };
    return LayoutFunctions;
}());
new LayoutFunctions();
