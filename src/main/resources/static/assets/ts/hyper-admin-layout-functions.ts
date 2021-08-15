class LayoutFunctions {

    searchSugestion!: HTMLDivElement;
    sidenavCollapse!: NodeListOf<HTMLDivElement>;
    sidenavCollapseBtn!: NodeListOf<HTMLDivElement>;
    isDisabled: boolean = false;

    constructor() {
        this.searchSugestion = document.querySelector('.search-form .dropdown-menu')! as HTMLDivElement;
        this.sidenavCollapse = document.querySelectorAll('.side-nav .accordion-collapse')! as NodeListOf<HTMLDivElement>;
        this.sidenavCollapseBtn = document.querySelectorAll('.side-nav .accordion-button')! as NodeListOf<HTMLDivElement>;
        this.checkStorage();
        this.init();
    }

    private checkStorage() {
        if (typeof (Storage) !== "undefined" && localStorage.getItem("hyperSide")) {
            document.body.classList.add('hide-side-nav');
            this.accordionState(true);
        }
    }

    private accordionState(state: boolean) {
        if (state) {
            this.sidenavCollapse.forEach((Element) => {
                Element.classList.add('show');
            });
            this.sidenavCollapseBtn.forEach((Element) => {
                Element.setAttribute('aria-expanded' , 'true');
            });
            this.isDisabled = true;
        } else {
            this.sidenavCollapse.forEach((Element) => {
                Element.classList.remove('show');
            });
            this.sidenavCollapseBtn.forEach((Element) => {
                Element.setAttribute('aria-expanded' , 'false');
                Element.classList.add('collapsed');
            });
            this.isDisabled = false;
        }
    }

    private init() {
        document.querySelector('.setting-btn-item .btn')!.addEventListener("click", () => {
            document.body.classList.toggle('show-setting-bar');
        });
        document.querySelector('.hamburger-btn')!.addEventListener("click", () => {
            document.body.classList.toggle('hide-side-nav');
            if (document.body.classList.contains('hide-side-nav')) {
                localStorage.setItem("hyperSide" , "active");
                this.accordionState(true);
            } else {
                localStorage.removeItem("hyperSide");
                this.accordionState(false);
            }
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
            }, 100);
        });
        document.querySelectorAll('.side-nav .accordion').forEach((Element) => {
            Element.addEventListener('hide.bs.collapse', (e) => {
                if (this.isDisabled) {
                    e.preventDefault();
                }
            });
        });
    }

}

new LayoutFunctions();