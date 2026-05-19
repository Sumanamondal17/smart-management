console.log("Contacts.js")
const baseURL = window.location.origin;
const viewContactModal=document.getElementById("view_contact_modal")
// options with default values
const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
  id: 'view_contact_modal',
  override: true
};

const contactModal=new Modal(viewContactModal,options,instanceOptions)

function openContactModal(){
    contactModal.show();
}
function closeContactModal(){
    contactModal.hide();
}
async function loadContactData(id){
    console.log(id);
    try{
        const data=await(
        await fetch(`${baseURL}/api/contacts/${id}`)
        
        ).json();
        console.log(data);
        document.querySelector('#contact_name').innerHTML=data.name;
        document.querySelector('#contact_email').innerHTML=data.email;
        document.querySelector('#contact_address').innerHTML=data.address;
        document.querySelector('#contact_phoneNumber').innerHTML=data.phoneNumber;
      

        document.querySelector('#contact_profilePic').src = data.profilePic; 
        document.querySelector('#contact_description').innerHTML=data.description;
        const contactFavorite=document.querySelector('#contact_favorite');
        if(data.favorite){
            contactFavorite.innerHTML="<i class='fa-solid fa-heart'></i>";
        }else{
            contactFavorite.innerHTML="<i class='fa-regular fa-heart'></i>";
        }
        openContactModal();

    }catch(error){
        console.log("Error:",error);
    }

}

//delete contact
async function deleteContact(id){
    Swal.fire({
  title: "Do you want to delete the contact?",
  icon:"warning",
  showCancelButton: true,
  confirmButtonText: "Yes",
 
}).then((result) => {
  /* Read more about isConfirmed, isDenied below */
  if (result.isConfirmed) {
    const url=`${baseURL}/user/contacts/delete/`+id;
    window.location.replace(url);
  }
});

}