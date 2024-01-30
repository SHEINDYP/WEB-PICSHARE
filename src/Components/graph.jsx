import React, { useEffect, useState } from 'react'
// import * as React from 'react';
import { BarChart } from '@mui/x-charts/BarChart';


export default function Graph(props) {
  const [arr, setArr] = useState()


  // useEffect(()=>{
  //   setArr(props.counts)
  // },[props.counts])


  return (
    <>
      <BarChart
      xAxis={[{ scaleType: 'band', data: props.counts.map((count, index) => `Week ${index + 1}`)}]}
      series = {[{ data: props.counts }]}
      // xAxis={[{ scaleType: 'band', data: ['week 1', 'week 2', 'week 3', 'week 4', 'week5'] }]}
      // series={[{ data: props.counts }]}
      width={500}
      height={300}
    />

    </>
  )

}