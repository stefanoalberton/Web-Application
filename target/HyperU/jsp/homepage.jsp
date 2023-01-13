<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html lang="en">

  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-wEmeIV1mKuiNpC+IOBjI7aAzPcEZeedi5yW5f2yOq55WWLwNGmvvx4Um1vskeMj0" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" integrity="sha512-iBBXm8fW90+nuLcSKlbmrPcLa0OT92xO1BIsZ+ywDWZCvqsWgccV3gFoRBv0z+8dLJgyAHIhR35VZc2oM/gI1w==" crossorigin="anonymous" />

    <title>HyperU</title>
  </head>


  <body>
    <!-- Header fittizio temporaneo -->
    <div class="navbar navbar-dark bg-dark shadow-sm">
        <div class="navbar-brand d-flex align-items-center">
            <strong>Header</strong>
        </div>
    </div>

    </div>

        <div class="row ">
            
            <!-- SIDE BAR SINISTRA TEMPORANEA-->
            <div class="col-3 themed-grid-col text-center"> 1 </div>

            <div class="col-6 themed-grid-col ">
                    <div class="card shadow-sm m-5">

                        <div class="card-body">
                            
                            <div class="col d-flex justify-content-start align-items-center small">
                                <img src="user.png" alt="user" class="me-2 rounded-circle" width="24" height="24">
                                <span class="card-text">Luca martinelli</span>
                                <span class="mx-2"> - </span>
                                <span class="card-text text-muted">3/6/2021 15:18</span>
                            </div>
                            
                            <div class="mt-3">
                                <h5 class="card-title">HyperU </h5>
                                <div class="">A webapp in which users can share ideas...</div>
                            
                                <div class="font-monospace small my-2">
                                    <a class="link-success text-decoration-none" href="#">#web-application</a>
                                    <a class="link-success text-decoration-none" href="#">#database</a>
                                </div>

                                <div >
                                    <a href="#"><span class=" badge rounded-pill bg-primary ">Python</span></a>
                                    <a href="#"><span class=" badge rounded-pill bg-primary ">Java</span></a>
                                </div>        
                            </div>
                            
                           

                        </div>

                        <div class="container px-3 pb-3">
                            <img src="logo.png" class="card-img rounded " alt="immagine">
                        </div>        

                        <div class="card-footer d-flex justify-content-between align-items-center">
                                <div class="col">
                                    <span class="mx-2">
                                        <span class="far fa-heart" ></span>
                                        <span> 1518 </span>
                                    </span>
                                    <span class="mx-2">
                                        <span class="far fa-comment"></span>
                                        <span> 36 </span>
                                    </span>
                                </div>
                            
                                <div class="col d-flex justify-content-end">
                                    <button class="btn btn-secondary btn-sm" type="button"> 
                                        <i class="fas fa-user-friends mx-1"> </i>
                                        <span> Join Team </span>
                                    </button>
                                </div>
                        </div>

                    </div>
            </div>


            <!-- SIDE BAR DESTRA TEMPORANEA-->
            <div class="col-3 themed-grid-col text-center">3</div>

    </div>

    
    <!-- Optional JavaScript; choose one of the two! -->
    <!-- Option 1: Bootstrap Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-p34f1UUtsS3wqzfto5wAAmdvj+osOnFyQFpp4Ua3gs/ZVWx6oOypYoCJhGGScy+8" crossorigin="anonymous"></script>
  </body>
</html>