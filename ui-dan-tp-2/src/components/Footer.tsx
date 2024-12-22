import React from "react";

function Footer() {
  return (
    <div className='py-10 flex justify-center item-center'>
      {/* // <aside> */}
        <svg
        width={"50px"}
        enableBackground="new 0 0 595.3 699.4"
        imageRendering="optimizeQuality"
        shapeRendering="geometricPrecision"
        textRendering="geometricPrecision"
        viewBox="0 0 595.3 699.4"
        xmlns="http://www.w3.org/2000/svg"
        >
          <path
            clipRule="evenodd"
            d="m246.6 0h102v190.8c80.8-22.4 140.4-96.7 140.4-184.4h106.3c0 146.5-106.8 268.9-246.6 293.2v4.4h233.9v104.2h-214.4c130 31.8 227 149.5 227 289.1h-106.2c0-87.7-59.6-162-140.3-184.4v186.5h-102v-186.5c-80.7 22.4-140.3 96.7-140.3 184.4h-106.4c0-139.6 97-257.3 227-289.1h-214.2v-104.2h233.9v-4.4c-139.9-24.3-246.7-146.7-246.7-293.2h106.3c0 87.7 59.6 162 140.3 184.4z"
            fillRule="evenodd"
          />
        </svg>
        <p>
          UTN FRSF Industries Ltd.
          <br />© 2024. Todos los derechos reservados.
        </p>
       {/* </aside> */}
    </div>
    // <>
    //   <footer className="footer p-4 bg-base-200 text-base-content">
    //     <aside>
    //       <svg
    //         width={"50px"}
    //         enableBackground="new 0 0 595.3 699.4"
    //         imageRendering="optimizeQuality"
    //         shapeRendering="geometricPrecision"
    //         textRendering="geometricPrecision"
    //         viewBox="0 0 595.3 699.4"
    //         xmlns="http://www.w3.org/2000/svg"
    //       >
    //         <path
    //           clipRule="evenodd"
    //           d="m246.6 0h102v190.8c80.8-22.4 140.4-96.7 140.4-184.4h106.3c0 146.5-106.8 268.9-246.6 293.2v4.4h233.9v104.2h-214.4c130 31.8 227 149.5 227 289.1h-106.2c0-87.7-59.6-162-140.3-184.4v186.5h-102v-186.5c-80.7 22.4-140.3 96.7-140.3 184.4h-106.4c0-139.6 97-257.3 227-289.1h-214.2v-104.2h233.9v-4.4c-139.9-24.3-246.7-146.7-246.7-293.2h106.3c0 87.7 59.6 162 140.3 184.4z"
    //           fillRule="evenodd"
    //         />
    //       </svg>
    //       <p>
    //         UTN FRSF Industries Ltd.
    //         <br />© 2023. Todos los derechos reservados.
    //       </p>
    //     </aside>
    //     <nav>
    //       <header className="footer-title">Sucursales</header>
    //       <div
    //         className="m"
    //           style={{
    //             display: "flex",
    //             alignItems: "center",
    //             marginBottom: "10px",
    //           }}
    //         >
    //           <svg
    //             xmlns="http://www.w3.org/2000/svg"
    //             width="24"
    //             height="24"
    //             viewBox="0 0 24 24"
    //             fill="none"
    //             stroke="currentColor"
    //             strokeWidth="2"
    //             strokeLinecap="round"
    //             strokeLinejoin="round"
    //             style={{ marginRight: "10px" }}
    //           >
    //             <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path>
    //             <circle cx="12" cy="10" r="3"></circle>
    //           </svg>
    //           <p>Bv Pellegrini 2045 (Santa Fe)</p>
    //         </div>
    //         <div
    //         className="m"
    //           style={{
    //             display: "flex",
    //             alignItems: "center",
    //             marginBottom: "10px",
    //           }}
    //         >
    //           <svg
    //             xmlns="http://www.w3.org/2000/svg"
    //             width="24"
    //             height="24"
    //             viewBox="0 0 24 24"
    //             fill="none"
    //             stroke="currentColor"
    //             strokeWidth="2"
    //             strokeLinecap="round"
    //             strokeLinejoin="round"
    //             style={{ marginRight: "10px" }}
    //           >
    //             <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path>
    //             <circle cx="12" cy="10" r="3"></circle>
    //           </svg>
    //           <p>Rivadavia 4968 (Rafaela)</p>
    //         </div>
    //         <div
    //         className="m"
    //           style={{
    //             display: "flex",
    //             alignItems: "center",
    //             marginBottom: "10px",
    //           }}
    //         >
    //           <svg
    //             xmlns="http://www.w3.org/2000/svg"
    //             width="24"
    //             height="24"
    //             viewBox="0 0 24 24"
    //             fill="none"
    //             stroke="currentColor"
    //             strokeWidth="2"
    //             strokeLinecap="round"
    //             strokeLinejoin="round"
    //             style={{ marginRight: "10px" }}
    //           >
    //             <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path>
    //             <circle cx="12" cy="10" r="3"></circle>
    //           </svg>
    //           <p>Belgrano 2496 (Esperanza)</p>
    //         </div>
    //     </nav>
    //     <nav>
    //       <header className="footer-title">Contactos</header>
    //       <nav>
            
    //         <div
    //           style={{
    //             display: "flex",
    //             alignItems: "center",
    //             marginBottom: "10px",
    //           }}
    //         >
    //           <svg
    //             xmlns="http://www.w3.org/2000/svg"
    //             width="24"
    //             height="24"
    //             viewBox="0 0 24 24"
    //             fill="none"
    //             stroke="currentColor"
    //             strokeWidth="2"
    //             strokeLinecap="round"
    //             strokeLinejoin="round"
    //             style={{ marginRight: "10px" }}
    //           >
    //             <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"></path>
    //           </svg>
    //           <p>+(0342) 496-1978</p>
    //         </div>
    //         <div
    //           style={{
    //             display: "flex",
    //             alignItems: "center",
    //             marginBottom: "10px",
    //           }}
    //         >
    //           <svg
    //             xmlns="http://www.w3.org/2000/svg"
    //             width="24"
    //             height="24"
    //             viewBox="0 0 24 24"
    //             fill="none"
    //             stroke="currentColor"
    //             strokeWidth="2"
    //             strokeLinecap="round"
    //             strokeLinejoin="round"
    //             style={{ marginRight: "10px" }}
    //           >
    //             <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"></path>
    //             <polyline points="22,6 12,13 2,6"></polyline>
    //           </svg>
    //           <p>inmobiliariaFRSF@gmail.com</p>
    //         </div>
    //       </nav>
    //     </nav>
    //   </footer>
    // </>
  );
}

export default Footer;