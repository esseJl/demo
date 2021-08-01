$(document).ready(function () {
    $('.table .eBtn').on('click', function (event) {
        event.preventDefault();
        var href = $(this).attr('href');
        $.get(href, function (brand, status) {
            $('.myForm #brandId').val(brand.brandUUID);
            $('.myForm #brandNameId').val(brand.brandName);

        });
        $('.myForm #exampleModal').modal('show');
    });

    $('.table .ePBtn').on('click', function (event) {
        event.preventDefault();
        var url = $(this).attr('href');

        $.ajax({
            type: "GET",
            contentType: "application/json; charset=utf-8",
            url: url,
            dataType: "json",
            success: function (image) {
                $('.myForm2 #imageId').val(image.imageUUID);
                $('.myForm2 #imageNameId').val(image.imageName);
                $('.myForm2 img').attr('src', url);
            }
        });

        $('.myForm2 #updateImageModal').modal('show');
    });

    $('.container-fluid #edit-btn-category').on('click', function (event) {
        event.preventDefault();
        var url = $(this).attr('href');

        $.ajax({
            type: "GET",
            contentType: "application/json; charset=utf-8",
            url: url,
            dataType: "json",
            success: function (category) {
                $('#categoryId').val(category.categoryUUID);
                $('#categoryNameId').val(category.name);
            }
        });

        $('#edit_staticBackdrop').modal('show');
    });

    $('.btnForAddImageId').on('click', function (event) {
        event.preventDefault();

        var href = '/product/get/';
        var id = $(this).attr('href');
        href += id;
        $.get(href, function (product, status) {
            $('.modal #productId').val(product.productUUID);
            $('.modal #productName').val(product.name);
        });
        var href2 = '/image/product/' + id;
        $('#addNewImageId .clearfix').empty();
        $.get(href2, function (images, status) {
            $.each(images, function (key, value) {
                $app = '<button type="button" class="btn float-end m-1 p-0" onclick="">\n' +
                    ' <img class="shadow  rounded m-0 p-0  " src="/image/' + value.imageUUID + '" width="90px!important" height="80px!important"/>\n' +
                    '   </button>';
                //$app = $app.html();
                $('#addNewImageId .clearfix').append($app);
            });
        });

        $('#addNewImageId').modal('show');


    });


    //Use this code if button is appended in the DOM
    $(document).on('click', '#addNewImageId .clearfix button img', function (event) {
        event.preventDefault();
        $imgsrc = $(this).attr('src');
        //alert($imgsrc);
        $('#full4imgId').attr('src', $imgsrc);
        $('#deleteBtnId').attr('href', $imgsrc + '/delete');
        $('#addNewImageId').modal('hide');
        $('#exampleModalToggle4img').modal('show');
    });
});