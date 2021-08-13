"use strict";
var LayoutFunctions = /** @class */ (function () {
    function LayoutFunctions() {
        this.searchSugestion = document.querySelector('.search-form .dropdown-menu');
        this.init();
    }
    LayoutFunctions.prototype.init = function () {
        var _this = this;
        document.querySelector('.setting-btn-item .btn').addEventListener("click", function () {
            document.body.classList.toggle('show-setting-bar');
        });
        document.querySelector('.hamburger-btn').addEventListener("click", function () {
            document.body.classList.toggle('hide-side-nav');
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
            });
        });
    };
    return LayoutFunctions;
}());
new LayoutFunctions();
