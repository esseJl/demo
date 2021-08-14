class LayoutFunctions {
    searchSugestion!: HTMLDivElement;
    sidenavCollapse!: NodeListOf<HTMLDivElement>;
    isDisabled:boolean = false;
    constructor() {
        this.searchSugestion = document.querySelector('.search-form .dropdown-menu')! as HTMLDivElement;
        this.sidenavCollapse = document.querySelectorAll('.side-nav .accordion-collapse')! as NodeListOf<HTMLDivElement>;
        this.init();
    }
    private checkStorage() {
        if (typeof (Storage) !== "undefined") {
            localStorage.setItem("hyperSetting", "hide-side-nav");
            if (localStorage.getItem("hyperSetting")) {
                document.body.classList.add('hide-side-nav');
            }
        }
        this.sidenavCollapse.forEach((Element) => {
            Element.classList.add('show');
        });

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
        document.querySelectorAll('.side-nav .accordion').forEach((Element) => {
            Element.addEventListener('hide.bs.collapse', (e) => {
                if(isDisabled){
                    e.preventDefault();
                }
            });
        });
    }
}
new LayoutFunctions();