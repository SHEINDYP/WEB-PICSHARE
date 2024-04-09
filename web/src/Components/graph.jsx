import React, { useEffect, useState } from 'react'
// import * as React from 'react';
import { BarChart } from '@mui/x-charts/BarChart';


export default function Graph(props) {

  const notConnected = () => {
    swal({
      title: "אופס... אתה לא מחובר",
      buttons: {
        login: {
          text: "התחבר",
          value: "login",
        },
        cancel: "בטל",
      },
    })
      .then((value) => {
        switch (value) {
          case "login":
            navigate('/SignIn')
        }
      });
  }

  return (
    <>
      <BarChart
        xAxis={[{ scaleType: 'band', data:props.counts!=null ?  (props.counts.map((count, index) => `Week ${index + 1}`)): notConnected  }]}
        series={[{ data: props.counts }]}
        width={500}
        height={300}
      />

    </>
  )

}