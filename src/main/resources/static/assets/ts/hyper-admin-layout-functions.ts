class LayoutFunctions {
    searchSugestion!: HTMLDivElement;
    constructor() {
        this.searchSugestion = document.querySelector('.search-form .dropdown-menu')! as HTMLDivElement;
        this.init();
    }
    private init() {
        document.querySelector('.setting-btn-item .btn')!.addEventListener("click", () => {
            document.body.classList.toggle('show-setting-bar');
        });
        document.querySelector('.hamburger-btn')!.addEventListener("click", () => {
            document.body.classList.toggle('hide-side-nav');
        });
        document.querySelector('.search-form input')!.addEventListener("focus", () => {
            this.searchSugestion.classList.add('show');
        });
        document.querySelector('.setting-bar .bar-title a')!.addEventListener("click", () => {
            document.body.classList.remove('show-setting-bar');
        });
        document.querySelector('.search-form input')!.addEventListener("blur", () => {
            setTimeout(() => {
                this.searchSugestion.classList.remove('show');
            });
        });
    }
}
new LayoutFunctions();